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
import java.net.URL;
import java.util.Arrays;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;

@FunctionalInterface
public interface OnNavigateToEventListener extends WebDriverEventListener<Navigation> {
	void onTo(WebDriver driver, Navigation navigation, String url);

	@Override
	default void invoke(WebDriver driver, Navigation navigation, Object proxy, Method method, Object[] args, Object returnValue) {
		onTo(driver, navigation, args[0].toString());
	}
	
	@Override
	default boolean isApplicable(Object obj, Class<?>[] parameterTypes) {
		return Navigation.class.isInstance(obj) &&
			(Arrays.equals(parameterTypes, new Class<?>[]{String.class}) || Arrays.equals(parameterTypes, new Class<?>[]{URL.class}));
	}
}
