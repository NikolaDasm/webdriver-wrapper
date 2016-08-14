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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.TouchScreen;

import nikoladasm.webdriver.wrapper.listeners.WebDriverEventListener;

public final class TouchScreenWrapperFactory {

	private static class InvocationHandlerImpl implements InvocationHandler {

		private final TouchScreen touchScreen;
		private final WebDriver driver;
		private final Listeners listeners;
		
		public InvocationHandlerImpl(
				TouchScreen touchScreen,
				WebDriver driver,
				Listeners listeners) {
			this.touchScreen = touchScreen;
			this.driver = driver;
			this.listeners = listeners;
		}
		
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			String methodName = method.getName();
			if ("getWrappedTouchScreen".equals(methodName))
				return touchScreen;
			Class<?>[] parameterTypes = method.getParameterTypes();
			WebDriverEventListener<TouchScreen> listener;
			if ((listener = listeners.getBeforeListener(methodName)) != null && listener.isApplicable(proxy, parameterTypes))
				listener.invoke(driver, touchScreen, proxy, method, args, null);
			Object result;
			try {
				result = method.invoke(touchScreen, args);
			} catch (InvocationTargetException e) {
				Throwable t = e.getTargetException();
				if (listeners.getExeptionListener() != null)
					listeners.getExeptionListener().invoke(driver, touchScreen, proxy, method, args, t);
				throw t;
			}
			if ((listener = listeners.getAfterListener(methodName)) != null && listener.isApplicable(proxy, parameterTypes))
				listener.invoke(driver, touchScreen, proxy, method, args, result);
			return result;
		}
	}
	
	public static TouchScreen wrapTouchScreen(
			TouchScreen touchScreen,
			WebDriver driver,
			Listeners listeners) {
		return (TouchScreen) Proxy.newProxyInstance(
			TouchScreenWrapperFactory.class.getClassLoader(),
			new Class<?>[]{TouchScreen.class, WrapsTouchScreen.class},
			new InvocationHandlerImpl(touchScreen, driver, listeners));
	}
	
	private TouchScreenWrapperFactory() {}
}
