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

import static nikoladasm.webdriver.wrapper.EventListenerLocation.ON_EXCEPTION;
import static nikoladasm.webdriver.wrapper.internal.TimeClause.BEFORE;

import nikoladasm.webdriver.wrapper.EventListenerLocation;
import nikoladasm.webdriver.wrapper.listeners.WebDriverEventListener;

public class ListenableBaseTest {

	protected final Listeners listeners = new Listeners();
	
	protected <T> void setListener(EventListenerLocation<T> method, T eventListener) {
		if (ON_EXCEPTION.equals(method)) {
			listeners.setExeptionListener((WebDriverEventListener<?>) eventListener);
		} else {
			if (method.timeClause() == BEFORE)
				listeners.setBeforeListener(method.name(), (WebDriverEventListener<?>) eventListener);
			else
				listeners.setAfterListener(method.name(), (WebDriverEventListener<?>) eventListener);
		}
	}
}
