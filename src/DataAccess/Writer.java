/*
 * StructED - Machine Learning, Structured Prediction Package written in Java.
 * Copyright (C) 2014 Yossi Adi, E-Mail: yossiadidrum@gmail.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package DataAccess;

import java.util.ArrayList;
import java.util.Map;

public interface Writer {
    public void writeData2File(String path, ArrayList<String> data, boolean isNewLine);
    public void writeHashMap2File(String path, Map<Integer, Double> data);
    public void writeScoresFile(String exampleName ,String path, Map<String, Double> data, int maxElements2Display);
    public void clearPrevResult(String path);
}
