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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.WebDriver.ImeHandler;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.WebDriver.Window;

import nikoladasm.webdriver.wrapper.listeners.WebDriverEventListener;

import static nikoladasm.webdriver.wrapper.internal.LogsWrapperFactory.wrapLogs;
import static nikoladasm.webdriver.wrapper.internal.ImeHandlerWrapperFactory.wrapImeHandler;
import static nikoladasm.webdriver.wrapper.internal.WindowWrapperFactory.wrapWindow;
import static nikoladasm.webdriver.wrapper.internal.TimeoutsWrapperFactory.wrapTimeouts;

public final class OptionsWrapperFactory {

	private static class InvocationHandlerImpl implements InvocationHandler {

		private final Options options;
		private final WebDriver driver;
		private final Listeners listeners;
		
		public InvocationHandlerImpl(
				Options options,
				WebDriver driver,
				Listeners listeners) {
			this.options = options;
			this.driver = driver;
			this.listeners = listeners;
		}
		
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			String methodName = method.getName();
			if ("getWrappedOptions".equals(methodName))
				return options;
			Class<?>[] parameterTypes = method.getParameterTypes();
			WebDriverEventListener<Options> listener;
			if ((listener = listeners.getBeforeListener(methodName)) != null && listener.isApplicable(proxy, parameterTypes))
				listener.invoke(driver, options, proxy, method, args, null);
			Object result;
			Object wrappedResult;
			try {
				result = method.invoke(options, args);
				wrappedResult = wrapResult(result, methodName, parameterTypes);
			} catch (InvocationTargetException e) {
				Throwable t = e.getTargetException();
				if (listeners.getExeptionListener() != null)
					listeners.getExeptionListener().invoke(driver, options, proxy, method, args, t);
				throw t;
			}
			if ((listener = listeners.getAfterListener(methodName)) != null && listener.isApplicable(proxy, parameterTypes))
				listener.invoke(driver, options, proxy, method, args, result);
			return wrappedResult;
		}

		private Object wrapResult(Object result, String methodName, Class<?>[] parameterTypes) {
			if ("logs".equals(methodName) && Arrays.equals(parameterTypes, new Class<?>[]{}))
				return wrapLogs((Logs) result, driver, listeners);
			if ("ime".equals(methodName) && Arrays.equals(parameterTypes, new Class<?>[]{}))
				return wrapImeHandler((ImeHandler) result, driver, listeners);
			if ("window".equals(methodName) && Arrays.equals(parameterTypes, new Class<?>[]{}))
				return wrapWindow((Window) result, driver, listeners);
			if ("timeouts".equals(methodName) && Arrays.equals(parameterTypes, new Class<?>[]{}))
				return wrapTimeouts((Timeouts) result, driver, listeners);
			return result;
		}
	}
	
	public static Options wrapOptions(
			Options options,
			WebDriver driver,
			Listeners listeners) {
		return (Options) Proxy.newProxyInstance(
			OptionsWrapperFactory.class.getClassLoader(),
			new Class<?>[]{Options.class, WrapsOptions.class},
			new InvocationHandlerImpl(options, driver, listeners));
	}
	
	private OptionsWrapperFactory() {}
}
