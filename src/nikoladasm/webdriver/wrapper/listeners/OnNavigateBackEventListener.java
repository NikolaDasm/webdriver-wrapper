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
import org.openqa.selenium.WebDriver.Navigation;

@FunctionalInterface
public interface OnNavigateBackEventListener extends WebDriverEventListener<Navigation> {
	void onBack(WebDriver driver, Navigation navigation);

	@Override
	default void invoke(WebDriver driver, Navigation navigation, Object proxy, Method method, Object[] args, Object returnValue) {
		onBack(driver, navigation);
	}
	
	@Override
	default boolean isApplicable(Object obj, Class<?>[] parameterTypes) {
		return Navigation.class.isInstance(obj) && Arrays.equals(parameterTypes, new Class<?>[]{});
	}
}
