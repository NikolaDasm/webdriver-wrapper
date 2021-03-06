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

package nikoladasm.webdriver.wrapper.listeners;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;

@FunctionalInterface
public interface OnGetCookieNamedAfterEventListener extends WebDriverEventListener<Options> {
	void onGetCookieNamed(WebDriver driver, Options options, String name, Cookie cookie);

	@Override
	default void invoke(WebDriver driver, Options options, Object proxy, Method method, Object[] args, Object returnValue) {
		onGetCookieNamed(driver, options, (String) args[0], (Cookie) returnValue);
	}
	
	@Override
	default boolean isApplicable(Object obj, Class<?>[] parameterTypes) {
		return Options.class.isInstance(obj) && Arrays.equals(parameterTypes, new Class<?>[]{String.class});
	}
}
