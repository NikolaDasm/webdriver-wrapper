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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.ImeHandler;

@FunctionalInterface
public interface OnGetActiveEngineAfterEventListener extends WebDriverEventListener<ImeHandler> {
	void onGetActiveEngine(WebDriver driver, ImeHandler imeHandler, String engine);

	@Override
	default void invoke(WebDriver driver, ImeHandler imeHandler, Object proxy, Method method, Object[] args, Object returnValue) {
		onGetActiveEngine(driver, imeHandler, (String) returnValue);
	}
	
	@Override
	default boolean isApplicable(Object obj, Class<?>[] parameterTypes) {
		return ImeHandler.class.isInstance(obj) && Arrays.equals(parameterTypes, new Class<?>[]{});
	}
}
