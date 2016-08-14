/*
 *  WebDriver Wrapper
 *  Copyright (C) 2016  Nikolay Platov
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package nikoladasm.webdriver.wrapper.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Timeouts;

import nikoladasm.webdriver.wrapper.listeners.WebDriverEventListener;

public final class TimeoutsWrapperFactory {

	private static class InvocationHandlerImpl implements InvocationHandler {

		private final Timeouts timeouts;
		private final WebDriver driver;
		private final Listeners listeners;
		
		public InvocationHandlerImpl(
				Timeouts timeouts,
				WebDriver driver,
				Listeners listeners) {
			this.timeouts = timeouts;
			this.driver = driver;
			this.listeners = listeners;
		}
		
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			String methodName = method.getName();
			if ("getWrappedTimeouts".equals(methodName))
				return timeouts;
			if ("equals".equals(methodName))
				return proxy.getClass().isInstance(args[0]) && args[0] == proxy;
			Class<?>[] parameterTypes = method.getParameterTypes();
			WebDriverEventListener<Timeouts> listener;
			if ((listener = listeners.getBeforeListener(methodName)) != null && listener.isApplicable(proxy, parameterTypes))
				listener.invoke(driver, timeouts, proxy, method, args, null);
			Object result;
			try {
				result = method.invoke(timeouts, args);
			} catch (InvocationTargetException e) {
				Throwable t = e.getTargetException();
				if (listeners.getExeptionListener() != null)
					listeners.getExeptionListener().invoke(driver, timeouts, proxy, method, args, t);
				throw t;
			}
			if ((listener = listeners.getAfterListener(methodName)) != null && listener.isApplicable(proxy, parameterTypes))
				listener.invoke(driver, timeouts, proxy, method, args, result);
			if (("implicitlyWait".equals(methodName) || "setScriptTimeout".equals(methodName) ||
				"pageLoadTimeout".equals(methodName)) && Arrays.equals(parameterTypes, new Class<?>[]{long.class, TimeUnit.class}))
				return proxy;
			else
				return result;
		}
	}
	
	public static Timeouts wrapTimeouts(
			Timeouts timeouts,
			WebDriver driver,
			Listeners listeners) {
		return (Timeouts) Proxy.newProxyInstance(
			TimeoutsWrapperFactory.class.getClassLoader(),
			new Class<?>[]{Timeouts.class, WrapsTimeouts.class},
			new InvocationHandlerImpl(timeouts, driver, listeners));
	}
	
	private TimeoutsWrapperFactory() {}
}
