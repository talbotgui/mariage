package com.github.talbotgui.mariage.rest.security;

import java.lang.reflect.Method;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.interceptor.JamonPerformanceMonitorInterceptor;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@Component
public class MonitoringAdvisor extends AbstractPointcutAdvisor {
	private static final long serialVersionUID = 1L;

	private transient final StaticMethodMatcherPointcut pointcut = new StaticMethodMatcherPointcut() {
		@Override
		public boolean matches(final Method method, final Class<?> targetClass) {
			return targetClass.isAnnotationPresent(RestController.class)
					|| targetClass.isAnnotationPresent(Service.class)
					|| targetClass.isAnnotationPresent(Repository.class);
		}
	};

	@Override
	public boolean equals(final Object obj) {
		return false;
	}

	@Override
	public Advice getAdvice() {
		return new JamonPerformanceMonitorInterceptor(true, true);
	}

	@Override
	public Pointcut getPointcut() {
		return this.pointcut;
	}

	@Override
	public int hashCode() {
		return 0;
	}

}
