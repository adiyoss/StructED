/*
 * The MIT License (MIT)
 *
 * StructED - Machine Learning Package for Structured Prediction
 *
 * Copyright (c) 2015 Yossi Adi, E-Mail: yossiadidrum@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.structed.models.algorithms;

import com.structed.constants.Consts;
import com.structed.constants.ErrorConstants;
import com.structed.data.Logger;
import com.structed.data.entities.Example;
import com.structed.data.entities.Vector;
import com.structed.models.ClassifierData;
import com.structed.utils.MathHelpers;

import java.util.ArrayList;

/**
 * Created by yossiadi on 10/12/15.
 * 
 */

public class MultiClassReject implements IUpdateRule {

    //data members
    double eta;
    double lambda;
    double beta;
    double p;
    double threshold_1; // ln(p*beta/(1-p))
    double threshold_2; // ln(beta)
    final double logConst = 0.5* threshold_2 - Math.log(0.5);

    @Override
    public void init(ArrayList<Double> args) {
        if(args.size() != Consts.MULTICLASS_REGECT_SIZE){
            Logger.error(ErrorConstants.UPDATE_ARGUMENTS_ERROR);
            return;
        }
        //initialize the parameters
        this.eta = args.get(0);
        this.lambda = args.get(1);
        this.beta = args.get(2);
        this.p = args.get(3);

        this.threshold_1 = Math.log(beta * p / (1 - p));
        this.threshold_1 = Math.log(beta);
    }

    @Override
    public Vector update(Vector currentWeights, Example example, ClassifierData classifierData) {
        double newEta = eta/Math.sqrt(classifierData.iteration);
        Example phiRealLabel = classifierData.phi.convert(example, example.getLabel(), classifierData.kernel);
        double f = MathHelpers.multipleVectors(currentWeights, phiRealLabel.getFeatures());
        Vector newW = MathHelpers.mulScalarWithVectors(currentWeights, (1 - newEta * lambda));

        if(f < threshold_1)
            newW = MathHelpers.addScalar2Vectors(newW, eta*(1-p));
        else if(f >= threshold_1 && f < threshold_2)
            newW = MathHelpers.add2Vectors(newW, MathHelpers.mulScalarWithVectors(phiRealLabel.getFeatures(), eta * beta / (beta + Math.exp(f))));
        else {
            double l = -0.5 * f + this.logConst;
            if (l > 0)
                newW = MathHelpers.add2Vectors(newW, MathHelpers.mulScalarWithVectors(phiRealLabel.getFeatures(), eta / 2 ));
        }

        return newW;
    }

    public double getThreshold(){ return this.threshold_1; }
}
