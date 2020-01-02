/**
 * Copyright 2019 Pramati Prism, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.hyscale.dockerfile.gen.services.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import io.hyscale.commons.annotations.ComponentInterceptor;
import io.hyscale.commons.component.IInterceptorProcessor;
import io.hyscale.commons.exception.HyscaleException;
import io.hyscale.commons.utils.HyscaleContextUtil;
import io.hyscale.dockerfile.gen.services.model.DockerfileGenContext;
import io.hyscale.servicespec.commons.model.service.ServiceSpec;

@Aspect
@Configuration
public class DockerfileGenAspect {

    /**
     * Pointcut expression to catch methods annotated with {@link ComponentInterceptor} 
     * and has {@link ServiceSpec} and {@link DockerfileGenContext} as parameter
     * @param interceptor
     * @param context
     */
    @Pointcut("execution(* io.hyscale.dockerfile.gen.services.generator.DockerfileGenerator.*(..)) "
            + "&& @annotation(interceptor) && args(serviceSpec, context)")
    public void dockerfileGenPointCut(ComponentInterceptor interceptor, ServiceSpec serviceSpec,
            DockerfileGenContext context) {
    }

    @Before("dockerfileGenPointCut(interceptor, serviceSpec, context)")
    public void doBefore(JoinPoint jp, ComponentInterceptor interceptor, ServiceSpec serviceSpec,
            DockerfileGenContext context) throws HyscaleException {
        for (Class<? extends IInterceptorProcessor> processor : interceptor.processors()) {
            IInterceptorProcessor processorBean = HyscaleContextUtil.getSpringBean(processor);
            if (processorBean != null) {
                processorBean.before(serviceSpec, context);
            }
        }
    }

    @AfterReturning("dockerfileGenPointCut(interceptor, serviceSpec, context)")
    public void doAfter(JoinPoint jp, ComponentInterceptor interceptor, ServiceSpec serviceSpec,
            DockerfileGenContext context) throws HyscaleException {
        for (Class<? extends IInterceptorProcessor> processor : interceptor.processors()) {
            IInterceptorProcessor processorBean = HyscaleContextUtil.getSpringBean(processor);
            if (processorBean != null) {
                processorBean.afterRunning(serviceSpec, context);
            }
        }
    }
}
