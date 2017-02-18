package com.jxjxgo.apigateway.base

import java.lang.reflect.{Method, Parameter}
import java.util

import com.jxjxgo.apigateway.annotations.{ApiMapping, Param}
import com.jxjxgo.apigateway.enumerate.ParamSource
import com.jxjxgo.apigateway.server.HttpService
import com.jxjxgo.common.exception.ErrorCode
import org.apache.commons.lang.StringUtils
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable

/**
  * Created by fangzhongwei on 2017/2/18.
  */
object ApiConfigContext {
  private[this] val logger: Logger = LoggerFactory.getLogger(getClass)

  class ActionMethodParamAttribute(val source: ParamSource,
                                   val name: String,
                                   val classType: Class[_],
                                   val required: Boolean,
                                   val mask: String,
                                   val minLength: Int,
                                   val maxLength: Int,
                                   val min: Int,
                                   val max: Int,
                                   val errorCode: ErrorCode)

  private[this] val apiMap: mutable.Map[Int, (Method, Option[Class[_]], ApiMapping, scala.collection.mutable.Seq[ActionMethodParamAttribute])] = scala.collection.mutable.Map()

  def initActionMap: Unit = {
    val classList: util.ArrayList[Class[_]] = HttpService.getActionBeanClassList
    val iterator: util.Iterator[Class[_]] = classList.iterator()
    var clazz: Class[_] = null
    while (iterator.hasNext) {
      clazz = iterator.next()
      val declaredMethods: Array[Method] = clazz.getDeclaredMethods
      for (i <- 0 to declaredMethods.length - 1) {
        val method: Method = declaredMethods(i)
        val apiMapping: ApiMapping = method.getAnnotation[ApiMapping](classOf[ApiMapping])
        if (apiMapping != null) {
          val types: Array[Class[_]] = method.getParameterTypes
          apiMap.get(apiMapping.id()) match {
            case Some(tuple) =>
              logger.error(s"api ${apiMapping.id()} conflict: ${tuple._1.toGenericString}, ${method.toGenericString}, system will shutdown ...")
              System.exit(1)
            case None =>
              var actionMethodParamAttributeSeq: scala.collection.mutable.Seq[ActionMethodParamAttribute] = scala.collection.mutable.Seq[ActionMethodParamAttribute]()
              val parameters: Array[Parameter] = method.getParameters
              parameters.length match {
                case 0 => //actionMethodParamAttributeSeq = actionMethodParamAttributeSeq :+ None
                case _ => parameters.foreach {
                  p =>
                    val pv: Param = p.getAnnotation(classOf[Param])
                    (null == pv) match {
                      case true =>
                        logger.error(s"${p} has not Param Annotation, please add. system will shutdown ...")
                        System.exit(1)
                      case false =>
                        if (pv.source() != ParamSource.BODY && StringUtils.isBlank(pv.name())) {
                          logger.error(s"${pv.source} Param must has a non-blank name, method:$method, Param:$pv. system will shutdown ...")
                          System.exit(1)
                        }
                        actionMethodParamAttributeSeq = actionMethodParamAttributeSeq :+ new ActionMethodParamAttribute(pv.source(), pv.name(), p.getType, pv.required(), pv.mask(), pv.minLength(), pv.maxLength(), pv.min(), pv.max(), pv.error())
                    }
                }
              }

              if (actionMethodParamAttributeSeq.filter(_.source == ParamSource.BODY).length >= 2) {
                logger.error(s"There can only be one body source param in a action method, method: ${method.toGenericString}. now system will shutdown ...")
                System.exit(1)
              }

              val t: Option[Class[_]] = actionMethodParamAttributeSeq.zipWithIndex.filter(_._1.source == ParamSource.BODY).headOption match {
                case Some(tuple) => Some(types(tuple._2))
                case None => None
              }
              apiMap += (apiMapping.id() -> (method, t, apiMapping, actionMethodParamAttributeSeq))
          }
        }
      }
    }
  }

  def getAipConfig(apiId:Int): Option[(Method, Option[Class[_]], ApiMapping, mutable.Seq[ActionMethodParamAttribute])] = {
    apiMap.get(apiId)
  }

}
