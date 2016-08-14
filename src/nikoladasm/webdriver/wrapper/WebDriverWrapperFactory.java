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

package nikoladasm.webdriver.wrapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.internal.WrapsElement;

import nikoladasm.webdriver.wrapper.internal.Listeners;
import nikoladasm.webdriver.wrapper.internal.WebElementListenableWrapper;
import nikoladasm.webdriver.wrapper.listeners.WebDriverEventListener;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.Arrays.stream;

import static nikoladasm.webdriver.wrapper.EventListenerLocation.ON_EXCEPTION;
import static nikoladasm.webdriver.wrapper.internal.WebElementWrapperFactory.wrapWebElement;
import static nikoladasm.webdriver.wrapper.internal.TimeClause.*;
import static nikoladasm.webdriver.wrapper.internal.NavigationWrapperFactory.wrapNavigation;
import static nikoladasm.webdriver.wrapper.internal.TargetLocatorWrapperFactory.wrapTargetLocator;
import static nikoladasm.webdriver.wrapper.internal.OptionsWrapperFactory.wrapOptions;
import static nikoladasm.webdriver.wrapper.internal.KeyboardWrapperFactory.wrapKeyboard;
import static nikoladasm.webdriver.wrapper.internal.MouseWrapperFactory.wrapMouse;
import static nikoladasm.webdriver.wrapper.internal.TouchScreenWrapperFactory.wrapTouchScreen;

public final class WebDriverWrapperFactory {

	private static class InvocationHandlerImpl implements InvocationHandler {
		
		private final WebDriver driver;
		private final Listeners listeners = new Listeners();
		
		public InvocationHandlerImpl(WebDriver driver) {
			this.driver = driver;
		}
		
		@SuppressWarnings({ "rawtypes"})
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			String methodName = method.getName();
			if ("getWrappedDriver".equals(methodName))
				return driver();
			if ("setListener".equals(methodName)) {
				EventListenerLocation listenableMethod =
					(EventListenerLocation) args[0];
				WebDriverEventListener listener = (WebDriverEventListener) args[1];
				if (ON_EXCEPTION.equals(listenableMethod)) {
					listeners.setExeptionListener(listener);
				} else {
					if (listenableMethod.timeClause() == BEFORE)
						listeners.setBeforeListener(listenableMethod.name(), listener);
					else
						listeners.setAfterListener(listenableMethod.name(), listener);
				}
				return null;
			}
			if ("removeListener".equals(methodName)) {
				EventListenerLocation listenableMethod =
					(EventListenerLocation) args[0];
				if (ON_EXCEPTION.equals(listenableMethod)) {
					listeners.setExeptionListener(null);
				} else {
					if (listenableMethod.timeClause() == BEFORE)
						listeners.removeBeforeListener(listenableMethod.name());
					else
						listeners.removeAfterListener(listenableMethod.name());
				}
				return null;
			}
			WebDriverEventListener<Void> listener;
			Class<?>[] parameterTypes = method.getParameterTypes();
			if ((listener = listeners.getBeforeListener(methodName)) != null && listener.isApplicable(proxy, parameterTypes))
				listener.invoke(driver, null, proxy, method, args, null);
			Object result;
			Object wrappedResult;
			try {
				if (("executeScript".equals(methodName) || "executeAsyncScript".equals(methodName))
					&& Arrays.equals(parameterTypes, new Class<?>[]{String.class, Object[].class})) {
					Object[] uArgs = new Object[args.length];
					uArgs[0] = args[0];
					uArgs[1] = unwrap((Object[]) args[1]);
					result = method.invoke(driver, uArgs);
				} else {
					result = method.invoke(driver, args);
				}
				wrappedResult = wrapResult((WebDriver) proxy, result, methodName, parameterTypes);
			} catch (InvocationTargetException e) {
				Throwable t = e.getTargetException();
				if (listeners.getExeptionListener() != null)
					listeners.getExeptionListener().invoke(driver, null, proxy, method, args, t);
				throw t;
			}
			if ((listener = listeners.getAfterListener(methodName)) != null && listener.isApplicable(proxy, parameterTypes))
				listener.invoke(driver, null, proxy, method, args, result);
			return wrappedResult;
		}
		
		private WebDriver driver() {
			if (driver instanceof WrapsDriver)
				return ((WrapsDriver) driver).getWrappedDriver();
			return driver;
		}
		
		@SuppressWarnings("unchecked")
		private Object wrapResult(WebDriver wrappedDriver, Object result, String methodName, Class<?>[] parameterTypes) {
			if ("findElement".equals(methodName) && Arrays.equals(parameterTypes, new Class<?>[]{By.class}))
				return wrapWebElement((WebElement) result, driver, listeners);
			if ("findElements".equals(methodName) && Arrays.equals(parameterTypes, new Class<?>[]{By.class}))
				return ((List<WebElement>) result).stream().map(element -> wrapWebElement(element, driver, listeners)).collect(toList());
			if ("navigate".equals(methodName) && Arrays.equals(parameterTypes, new Class<?>[]{}))
				return wrapNavigation((Navigation) result, driver, listeners);
			if ("switchTo".equals(methodName) && Arrays.equals(parameterTypes, new Class<?>[]{}))
				return wrapTargetLocator((TargetLocator) result, driver, listeners, wrappedDriver);
			if ("manage".equals(methodName) && Arrays.equals(parameterTypes, new Class<?>[]{}))
				return wrapOptions((Options) result, driver, listeners);
			if ("getKeyboard".equals(methodName) && Arrays.equals(parameterTypes, new Class<?>[]{}))
				return wrapKeyboard((Keyboard) result, driver, listeners);
			if ("getMouse".equals(methodName) && Arrays.equals(parameterTypes, new Class<?>[]{}))
				return wrapMouse((Mouse) result, driver, listeners);
			if ("getTouch".equals(methodName) && Arrays.equals(parameterTypes, new Class<?>[]{}))
				return wrapTouchScreen((TouchScreen) result, driver, listeners);
			return result;
		}
		
		private Object[] unwrap(Object[] args) {
			return stream(args).map(this::unwrap).toArray();
		}
		
		private Object unwrap(Object arg) {
			if (arg instanceof List<?>)
				return ((List<?>) arg).stream().map(this::unwrap).collect(toList());
			if (arg instanceof Map<?, ?>)
				return ((Map<?, ?>) arg).entrySet().stream().collect(toMap(e -> e.getKey(), e -> unwrap(e.getKey())));
			if (arg instanceof WebElementListenableWrapper)
				return ((WrapsElement) arg).getWrappedElement();
			return arg;
		}
	}
	
	public static WebDriver wrapWebDriver(WebDriver driver) {
		return (WebDriver) Proxy.newProxyInstance(
			WebDriverWrapperFactory.class.getClassLoader(),
			extractInterfaces(driver),
			new InvocationHandlerImpl(driver));
	}
	
	private static Class<?>[] extractInterfaces(Object object) {
		Set<Class<?>> allInterfaces = new HashSet<>();
		allInterfaces.add(WrapsDriver.class);
		allInterfaces.add(WebDriverListenableWrapper.class);
		extractInterfaces(allInterfaces, object.getClass());
		return allInterfaces.toArray(new Class<?>[allInterfaces.size()]);
	}
	
	private static void extractInterfaces(Set<Class<?>> addTo, Class<?> clazz) {
		if (Object.class.equals(clazz))
			return;
		Class<?>[] classes = clazz.getInterfaces();
		addTo.addAll(Arrays.asList(classes));
		extractInterfaces(addTo, clazz.getSuperclass());
	}
	
	private WebDriverWrapperFactory() {}
}
