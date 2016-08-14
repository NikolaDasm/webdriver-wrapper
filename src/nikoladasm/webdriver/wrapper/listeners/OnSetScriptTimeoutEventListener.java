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
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Timeouts;

@FunctionalInterface
public interface OnSetScriptTimeoutEventListener extends WebDriverEventListener<Timeouts> {
	void onSetScriptTimeout(WebDriver driver, Timeouts timeouts, long time, TimeUnit unit);

	@Override
	default void invoke(WebDriver driver, Timeouts timeouts, Object proxy, Method method, Object[] args, Object returnValue) {
		onSetScriptTimeout(driver, timeouts, (long) args[0], (TimeUnit) args[1]);
	}
	
	@Override
	default boolean isApplicable(Object obj, Class<?>[] parameterTypes) {
		return Timeouts.class.isInstance(obj) && Arrays.equals(parameterTypes, new Class<?>[]{long.class, TimeUnit.class});
	}
}
