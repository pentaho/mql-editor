/*
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright 2008 Pentaho Corporation.  All rights reserved.
 */
package org.pentaho.gwt.widgets.client.colorpicker.images;

import com.google.gwt.user.client.ui.ImageBundle;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/* Images for the ColorPicker
	Copyright (c) 2007 John Dyer (http://johndyer.name)

	Permission is hereby granted, free of charge, to any person
	obtaining a copy of this software and associated documentation
	files (the "Software"), to deal in the Software without
	restriction, including without limitation the rights to use,
	copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the
	Software is furnished to do so, subject to the following
	conditions:

	The above copyright notice and this permission notice shall be
	included in all copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
	EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
	OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
	NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
	HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
	WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
	FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
	OTHER DEALINGS IN THE SOFTWARE.
	*/
public interface ColorPickerImageBundle extends ImageBundle
{
	public AbstractImagePrototype bar_blue_bl();
	public AbstractImagePrototype bar_blue_br();
	public AbstractImagePrototype bar_blue_tl();
	public AbstractImagePrototype bar_blue_tr();
	public AbstractImagePrototype bar_brightness();
	public AbstractImagePrototype bar_green_bl();
	public AbstractImagePrototype bar_green_br();
	public AbstractImagePrototype bar_green_tl();
	public AbstractImagePrototype bar_green_tr();
	public AbstractImagePrototype bar_red_bl();
	public AbstractImagePrototype bar_red_br();
	public AbstractImagePrototype bar_red_tl();
	public AbstractImagePrototype bar_red_tr();
	public AbstractImagePrototype bar_saturation();
	public AbstractImagePrototype bar_hue();
	public AbstractImagePrototype bar_white();
	public AbstractImagePrototype rangearrows();
	public AbstractImagePrototype map_blue_max();
	public AbstractImagePrototype map_blue_min();
	public AbstractImagePrototype map_brightness();
	public AbstractImagePrototype map_green_max();
	public AbstractImagePrototype map_green_min();
	public AbstractImagePrototype map_hue();
	public AbstractImagePrototype mappoint();
	public AbstractImagePrototype map_red_max();
	public AbstractImagePrototype map_red_min();
	public AbstractImagePrototype map_saturation();
	public AbstractImagePrototype map_saturation_overlay();
	public AbstractImagePrototype map_white();
}