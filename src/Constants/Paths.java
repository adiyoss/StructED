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

package Constants;

public class Paths {
    //SINGLETON
    private static Paths instance = new Paths();
    public static Paths getInstance(){
        return instance;
    }
    private Paths(){}

    //Train Path
    public String TRAIN_PATH = "";
    public String VALIDATION_PATH = "";

    //Weights
	public String OUTPUT_WEIGHTS_PATH = "";
    public String INIT_WEIGHTS_PATH = "";

    //Config Files
	public String CONFIG_PATH_TRAIN = "";
    public String CONFIG_PATH_TEST = "";
}

