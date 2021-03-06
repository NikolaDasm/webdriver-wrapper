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

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;

@FunctionalInterface
public interface OnDismissEventListener extends WebDriverEventListener<Alert> {
	void onDismiss(WebDriver driver, Alert alert);

	@Override
	default void invoke(WebDriver driver, Alert alert, Object proxy, Method method, Object[] args, Object returnValue) {
		onDismiss(driver, alert);
	}
	
	@Override
	default boolean isApplicable(Object obj, Class<?>[] parameterTypes) {
		return Alert.class.isInstance(obj) && Arrays.equals(parameterTypes, new Class<?>[]{});
	}
}
