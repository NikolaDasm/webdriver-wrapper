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
import org.openqa.selenium.WebDriver.Navigation;

import nikoladasm.webdriver.wrapper.listeners.WebDriverEventListener;

public final class NavigationWrapperFactory {

	private static class InvocationHandlerImpl implements InvocationHandler {

		private final Navigation navigation;
		private final WebDriver driver;
		private final Listeners listeners;
		
		public InvocationHandlerImpl(
				Navigation navigation,
				WebDriver driver,
				Listeners listeners) {
			this.navigation = navigation;
			this.driver = driver;
			this.listeners = listeners;
		}
		
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			String methodName = method.getName();
			if ("getWrappedNavigation".equals(methodName))
				return navigation;
			Class<?>[] parameterTypes = method.getParameterTypes();
			WebDriverEventListener<Navigation> listener;
			if ((listener = listeners.getBeforeListener(methodName)) != null && listener.isApplicable(proxy, parameterTypes))
				listener.invoke(driver, navigation, proxy, method, args, null);
			Object result;
			try {
				result = method.invoke(navigation, args);
			} catch (InvocationTargetException e) {
				Throwable t = e.getTargetException();
				if (listeners.getExeptionListener() != null)
					listeners.getExeptionListener().invoke(driver, navigation, proxy, method, args, t);
				throw t;
			}
			if ((listener = listeners.getAfterListener(methodName)) != null && listener.isApplicable(proxy, parameterTypes))
				listener.invoke(driver, navigation, proxy, method, args, result);
			return result;
		}
	}
	
	public static Navigation wrapNavigation(
			Navigation navigation,
			WebDriver driver,
			Listeners listeners) {
		return (Navigation) Proxy.newProxyInstance(
			NavigationWrapperFactory.class.getClassLoader(),
			new Class<?>[]{Navigation.class, WrapsNavigation.class},
			new InvocationHandlerImpl(navigation, driver, listeners));
	}
	
	private NavigationWrapperFactory() {}
}
