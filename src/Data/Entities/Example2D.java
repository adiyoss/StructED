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

package Data.Entities;

import java.util.ArrayList;

public class Example2D extends Example {

    private ArrayList<Vector> features2D;
    private ArrayList<Integer> labels2D;


    public Example2D(){
        features2D = new ArrayList<Vector>();
        labels2D = new ArrayList<Integer>();

    }

    public ArrayList<Vector> getFeatures2D() {
        return features2D;
    }

    public void setFeatures2D(ArrayList<Vector> features) {
        this.features2D = features;
    }

    public ArrayList<Integer> getLabels2D() { return labels2D; }

    public void setLabels2D(ArrayList<Integer> labels2D) { this.labels2D = labels2D; }
}
