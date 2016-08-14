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
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.interactions.internal.Coordinates;

@FunctionalInterface
public interface OnSingleTapEventListener extends WebDriverEventListener<TouchScreen> {
	void onSingleTap(WebDriver driver, TouchScreen touchScreen, Coordinates where);

	@Override
	default void invoke(WebDriver driver, TouchScreen touchScreen, Object proxy, Method method, Object[] args, Object returnValue) {
		onSingleTap(driver, touchScreen, (Coordinates) args[0]);
	}
	
	@Override
	default boolean isApplicable(Object obj, Class<?>[] parameterTypes) {
		return TouchScreen.class.isInstance(obj) && Arrays.equals(parameterTypes, new Class<?>[]{Coordinates.class});
	}
}
