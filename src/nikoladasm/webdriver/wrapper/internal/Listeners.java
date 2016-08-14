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

import java.util.HashMap;
import java.util.Map;

import nikoladasm.webdriver.wrapper.listeners.WebDriverEventListener;

public final class Listeners {
	private final Map<String, WebDriverEventListener<?>> beforeListeners = new HashMap<>();
	private final Map<String, WebDriverEventListener<?>> afterListeners = new HashMap<>();
	private WebDriverEventListener<?> exeptionListener;

	public void setBeforeListener(String methodName, WebDriverEventListener<?> eventListener) {
		beforeListeners.put(methodName, eventListener);
	}
	
	@SuppressWarnings("unchecked")
	public <T> WebDriverEventListener<T> getBeforeListener(String methodName) {
		return (WebDriverEventListener<T>) beforeListeners.get(methodName);
	}
	
	public void removeBeforeListener(String methodName) {
		beforeListeners.remove(methodName);
	}
	
	public void setAfterListener(String methodName, WebDriverEventListener<?> eventListener) {
		afterListeners.put(methodName, eventListener);
	}
	
	@SuppressWarnings("unchecked")
	public <T> WebDriverEventListener<T> getAfterListener(String methodName) {
		return (WebDriverEventListener<T>) afterListeners.get(methodName);
	}
	
	public void removeAfterListener(String methodName) {
		afterListeners.remove(methodName);
	}
	
	public void setExeptionListener(WebDriverEventListener<?> exeptionListener) {
		this.exeptionListener = exeptionListener;
	}

	@SuppressWarnings("unchecked")
	public <T> WebDriverEventListener<T> getExeptionListener() {
		return (WebDriverEventListener<T>) exeptionListener;
	}
}
