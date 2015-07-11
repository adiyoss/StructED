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

package com.structed.data.featurefunctions;

import com.structed.models.kernels1.IKernel;
import com.structed.constants.Consts;
import com.structed.data.CacheVowelData;
import com.structed.data.entities.Example;
import com.structed.data.Factory;
import com.structed.data.entities.Vector;
import com.structed.data.Logger;
import com.structed.utils.ConverterHelplers;
import com.structed.utils.MathHelpers;
import jsc.distributions.Gamma;

public class FeatureFunctionsVowelDuration implements IFeatureFunctions {

    int sizeOfVector = 116;
    final int win_size_1 = 1;
    final int small_offset = 1;
    final int offset_20 = 4;
    final int offset_30 = 6;
    final int offset_40 = 8;
    final int offset_50 = 10;
    final int offset_200 = 40;
    final int offset_180 = 36;
    final int offset_160 = 32;
    final int win_size_5 = 1;
    final int win_size_10 = 2;
    final int win_size_15 = 3;
    final int win_size_20 = 4;
    final int win_size_30 = 6;
    final int win_size_40 = 8;
    final int win_size_50 = 10;
    final double NORMALIZE = 0.01;
    final double NORMALIZE_BIG = 0.001;

    final int SHORT_TERM_ENERGY = 0;
    final int TOTAL_ENERGY = 1;
    final int LOW_ENERGY = 2;
    final int HIGH_ENERGY = 3;
    final int WIENER_ENTROPY = 4;
    final int AUTO_CORRELATION = 5;
    final int PITCH = 6;
    final int VOICING = 7;
    final int ZERO_CROSSING = 8;
    final int IS_VOWEL = 9;
    final int IS_NAZAL = 10;
    final int IS_GLIDE = 11;
    final int IS_SIL = 12;
    final int SUM_VOWELS = 13;
    final int SUM_NAZALS = 14;
    final int SUM_GLIDES = 15;
    final int MFCC_1 = 16;
    final int MFCC_2 = 17;
    final int MFCC_3 = 18;
    final int MFCC_4 = 19;
    final int F_1 = 20;
    final int F_2 = 21;

    @Override
    //return null on error
    public Example convert(Example example, String label, IKernel kernel) {

        try{
            Example newExample = Factory.getExample(0);
            newExample.sizeOfVector = sizeOfVector;

            String labelValues[] = label.split(Consts.CLASSIFICATION_SPLITTER);
            Vector phiFeatures = new Vector();

            //convert full vector
            if(ConverterHelplers.tryParseInt(labelValues[0])) {

                int start = Integer.parseInt(labelValues[0]);
                int end = Integer.parseInt(labelValues[1]);

                //=================calculate the features=================//
                //=========Difference 5 and 10 frames from location=======//
                int loc=0;

                //====Short Term Energy====//
                phiFeatures.put(loc, calculateDiff(example, win_size_10, end, SHORT_TERM_ENERGY));//short term energy, end location - 30 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_20, end, SHORT_TERM_ENERGY));//short term energy, end location - 30 window
                loc++;

                //====Total Energy====//
                phiFeatures.put(loc, calculateDiff(example, win_size_10, start, TOTAL_ENERGY));//total energy, start location - 20 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_15, start, TOTAL_ENERGY));//total energy, start location - 30 window
                loc++;
                phiFeatures.put(loc, Math.abs(calculateDiff(example, win_size_10, end, TOTAL_ENERGY)));//total energy, start location - 20 window
                loc++;
                phiFeatures.put(loc, Math.abs(calculateDiff(example, win_size_20, end, TOTAL_ENERGY)));//total energy, start location - 30 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_10, end - offset_50, TOTAL_ENERGY));//total energy, start location - 30 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_20, end - offset_50, TOTAL_ENERGY));//total energy, start location - 30 window
                loc++;

                //====Low Energy====//
                phiFeatures.put(loc, calculateDiff(example, win_size_10, start, LOW_ENERGY));//low energy, start location - 20 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_20, start, LOW_ENERGY));//low energy, start location - 30 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_30, start, LOW_ENERGY));//low energy, start location - 40 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_10, end, LOW_ENERGY));//low energy, end location - 20 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_20, end, LOW_ENERGY));//low energy, end location - 20 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_30, end, LOW_ENERGY));//low energy, end location - 30 window
                loc++;

                //====High Energy====//
                phiFeatures.put(loc, Math.abs(calculateDiff(example, win_size_10, end, HIGH_ENERGY)));//high energy, end location - 20 window
                loc++;
                phiFeatures.put(loc, Math.abs(calculateDiff(example, win_size_20, end, HIGH_ENERGY)));//high energy, end location - 30 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_10, end - offset_50, HIGH_ENERGY));//total energy, start location - 30 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_20, end - offset_50, HIGH_ENERGY));//total energy, start location - 30 window
                loc++;

                //====Wiener Entropy====//
                phiFeatures.put(loc, calculateDiff(example, win_size_10, start, WIENER_ENTROPY));//wiener entropy, start location - 20 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_20, start, WIENER_ENTROPY));//wiener entropy, start location - 30 window
                loc++;
                phiFeatures.put(loc, Math.abs(calculateDiff(example, win_size_10, end, WIENER_ENTROPY)));//wiener entropy, start location - 20 window
                loc++;
                phiFeatures.put(loc, Math.abs(calculateDiff(example, win_size_20, end, WIENER_ENTROPY)));//wiener entropy, start location - 30 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_10, end - offset_50, WIENER_ENTROPY));//total energy, start location - 30 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_20, end - offset_50, WIENER_ENTROPY));//total energy, start location - 30 window
                loc++;

                //===========Auto Correlation==========//
                phiFeatures.put(loc, calculateDiff(example, win_size_5, start, AUTO_CORRELATION));//auto correlation, start location - 5 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_10, start, AUTO_CORRELATION));//auto correlation, start location - 10 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_15, start, AUTO_CORRELATION));//auto correlation, start location - 15 window
                loc++;

                //==========Difference 5 and 15 frames from start=========//
                //====Pitch====//
                phiFeatures.put(loc, calculateDiff(example, win_size_10, start, PITCH));//pitch, start location - 20 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_20, start, PITCH));//pitch, start location - 30 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_10, end, PITCH));//pitch, start location - 20 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_20, end, PITCH));//pitch, start location - 30 window
                loc++;

                //====Voicing====//
                phiFeatures.put(loc, calculateDiff(example, win_size_10, start, VOICING));//voicing, start location - 15 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_20, start, VOICING));//voicing, start location - 20 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_20, start, VOICING));//voicing, start location - 30 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_10, end, VOICING));//voicing, end location - 30 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_20, end, VOICING));//voicing, end location - 30 window
                loc++;

                //====Zero-Crossing====//
                phiFeatures.put(loc, calculateDiff(example, win_size_20, start, ZERO_CROSSING));//zero-crossing, start location - 20 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_30, start, ZERO_CROSSING));//zero-crossing, start location - 30 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_20, end, ZERO_CROSSING));//zero-crossing, end location - 20 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_30, end, ZERO_CROSSING));//zero-crossing, end location - 30 window
                loc++;

                //=================Phoneme-Classifier==================//
                //====VOWELS - INDICATOR====//
                phiFeatures.put(loc, calculateDiff(example, win_size_10, start, IS_VOWEL));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_20, start, IS_VOWEL));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_30, start, IS_VOWEL));
                loc++;

                phiFeatures.put(loc, calculateDiff(example, win_size_10, end, IS_VOWEL));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_20, end, IS_VOWEL));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_30, end, IS_VOWEL));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_40, end, IS_VOWEL));
                loc++;

                //====NASAL - INDICATOR====//
                phiFeatures.put(loc, calculateDiff(example, win_size_10, start, IS_NAZAL));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_20, start, IS_NAZAL));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_10, end, IS_NAZAL));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_20, end, IS_NAZAL));
                loc++;

                //====GLIDE - INDICATOR====//
                phiFeatures.put(loc, calculateDiff(example, win_size_10, start, IS_GLIDE));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_20, start, IS_GLIDE));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_10, end, IS_GLIDE));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_20, end, IS_GLIDE));
                loc++;

                //====SIL - INDICATOR====//
                phiFeatures.put(loc, calculateDiff(example, win_size_10, start, IS_SIL));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_20, start, IS_SIL));
                loc++;

                phiFeatures.put(loc, calculateDiff(example, win_size_10, end, IS_SIL));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_20, end, IS_SIL));
                loc++;

                phiFeatures.put(loc, calculateDiff(example, win_size_10, end - offset_50, IS_SIL));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_20, end - offset_50, IS_SIL));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_30, end - offset_50, IS_SIL));
                loc++;

                //====VOWELS - SUM DIVIDE BY SUM ALL====//
                phiFeatures.put(loc, calculateDiff(example, win_size_10, start, SUM_VOWELS));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_20, start, SUM_VOWELS));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_30, start, SUM_VOWELS));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_10, end, SUM_VOWELS));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_20, end, SUM_VOWELS));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_30, end, SUM_VOWELS));
                loc++;

                //====NAZAL - SUM DIVIDE BY SUM ALL====//
                phiFeatures.put(loc, calculateDiff(example, win_size_10, start, SUM_NAZALS));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_20, start, SUM_NAZALS));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_30, start, SUM_NAZALS));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_10, end, SUM_NAZALS));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_20, end, SUM_NAZALS));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_30, end, SUM_NAZALS));
                loc++;

                //====GLIDE - SUM DIVIDE BY SUM ALL====//
                phiFeatures.put(loc, calculateDiff(example, win_size_10, start, SUM_GLIDES));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_20, start, SUM_GLIDES));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_30, start, SUM_GLIDES));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_10, end, SUM_GLIDES));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_20, end, SUM_GLIDES));
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_30, end, SUM_GLIDES));
                loc++;

                //======F1======//
                phiFeatures.put(loc, calculateDiff(example, win_size_20, start, F_1));//F1, end location - 20 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_30, start, F_1));//F1, end location - 20 window
                loc++;

                phiFeatures.put(loc, calculateDiff(example, win_size_20, end, F_1));//F1, end location - 20 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_30, end, F_1));//F1, end location - 20 window
                loc++;

                //======F2======//
                phiFeatures.put(loc, calculateDiff(example, win_size_20, start, F_2));//F2, end location - 20 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_30, start, F_2));//F2, end location - 20 window
                loc++;

                phiFeatures.put(loc, calculateDiff(example, win_size_20, end, F_2));//F2, end location - 20 window
                loc++;
                phiFeatures.put(loc, calculateDiff(example, win_size_30, end, F_2));//F2, end location - 20 window
                loc++;

                //==============Mean value from start to end==============//
                //true means prev the start point, false means after the end point
                phiFeatures.put(loc, calculateMean(example, start, end, SHORT_TERM_ENERGY, win_size_30, true));//Mean of short-term energy
                loc++;
                phiFeatures.put(loc, calculateMean(example, start, end, SHORT_TERM_ENERGY, win_size_30, false));//Mean of short-term energy
                loc++;
                phiFeatures.put(loc, calculateMean(example, start, end, LOW_ENERGY, win_size_30, true));//Mean of low energy
                loc++;
                phiFeatures.put(loc, calculateMean(example, start, end, LOW_ENERGY, win_size_30, false));//Mean of low energy
                loc++;
                phiFeatures.put(loc, calculateMean(example, start, end, WIENER_ENTROPY, win_size_30, true));//Mean of wiener entropy
                loc++;
                phiFeatures.put(loc, calculateMean(example, start, end, WIENER_ENTROPY, win_size_30, false));//Mean of wiener entropy
                loc++;
                phiFeatures.put(loc, calculateMean(example, start, end, VOICING, win_size_30, true));//Mean of voicing
                loc++;
                phiFeatures.put(loc, calculateMean(example, start, end, VOICING, win_size_30, false));//Mean of voicing
                loc++;
                phiFeatures.put(loc, calculateMean(example, start, end, ZERO_CROSSING, win_size_30, true));//Mean of zero-crossing
                loc++;
                phiFeatures.put(loc, calculateMean(example, start, end, ZERO_CROSSING, win_size_30, false));//Mean of zero-crossing
                loc++;
                phiFeatures.put(loc, calculateMean(example, start, end, IS_VOWEL, win_size_50, true));
                loc++;
                phiFeatures.put(loc, calculateMean(example, start, end, IS_VOWEL, win_size_30, false));
                loc++;
                phiFeatures.put(loc, calculateMean(example, start, end, IS_NAZAL, win_size_20, true));
                loc++;
                phiFeatures.put(loc, calculateMean(example, start, end, IS_NAZAL, win_size_20, false));
                loc++;
                phiFeatures.put(loc, calculateMean(example, start, end, IS_GLIDE, win_size_20, true));
                loc++;
                phiFeatures.put(loc, calculateMean(example, start, end, IS_GLIDE, win_size_20, false));
                loc++;
                phiFeatures.put(loc, calculateMean(example, start, end, SUM_VOWELS, win_size_50, true));
                loc++;
                phiFeatures.put(loc, calculateMean(example, start, end, SUM_VOWELS, win_size_30, false));
                loc++;

                //==============MFCC Feature Function==============//
                //=====================START=======================//
                phiFeatures.put(loc, NORMALIZE *example.getFeatures2D().get(start).get(MFCC_1));
                loc++;
                phiFeatures.put(loc, NORMALIZE *example.getFeatures2D().get(start).get(MFCC_2));
                loc++;
                phiFeatures.put(loc, NORMALIZE *example.getFeatures2D().get(start).get(MFCC_3));
                loc++;
                phiFeatures.put(loc, NORMALIZE *example.getFeatures2D().get(start).get(MFCC_4));
                loc++;

                //=======================END=======================//
                phiFeatures.put(loc, NORMALIZE *example.getFeatures2D().get(end).get(MFCC_1));
                loc++;
                phiFeatures.put(loc, NORMALIZE *example.getFeatures2D().get(end).get(MFCC_2));
                loc++;
                phiFeatures.put(loc, NORMALIZE *example.getFeatures2D().get(end).get(MFCC_3));
                loc++;
                phiFeatures.put(loc, NORMALIZE *example.getFeatures2D().get(end).get(MFCC_4));
                loc++;

                //===============Gamma Distribution Over The Vowel Length==============//
                //shape = mean^2/var
                //scale = var/mean
                double variance = Math.pow(Consts.STD_VOWEL_LENGTH,2);
                double shape = Math.pow(Consts.MEAN_VOWEL_LENGTH,2)/variance;
                double scale = variance/Consts.MEAN_VOWEL_LENGTH;
                Gamma gamma = new Gamma(shape, scale);
                double vowelLength = end - start;
                phiFeatures.put(loc,gamma.pdf(vowelLength)/gamma.pdf(Consts.MAX_VOWEL_GAMMA));
                loc++;

                //===============Gaussian Distribution Over The Vowel Length==============//
                double numerator = -Math.pow((vowelLength - Consts.MEAN_VOWEL_LENGTH),2);
                double denominator = 2*Math.pow(Consts.STD_VOWEL_LENGTH,2);
                phiFeatures.put(loc,Math.exp(numerator/denominator));
                loc++;

                newExample.setFeatures(phiFeatures);
                return newExample;
            }
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getSizeOfVector() {
        return this.sizeOfVector;
    }


    //**********************************FEATURE FUNCTIONS***************************************//
    //******************************************************************************************//
    //calculate the average difference of featureNumber win_size before and after location
    public double calculateDiff(Example example, int win_size, int location, int featureNumber)
    {
        try {
            double preVal;
            //get the cumulative values of featureNumber
            double startVal = CacheVowelData.getCumulativeValue(example, location - win_size, featureNumber);
            double endVal = CacheVowelData.getCumulativeValue(example, location, featureNumber);

            preVal = endVal - startVal;
            preVal /= win_size;


            double afterVal;
            //get the cumulative values of featureNumber
            startVal = endVal;
            endVal = CacheVowelData.getCumulativeValue(example, location + win_size, featureNumber);

            afterVal = endVal - startVal;
            afterVal /= win_size;

            Double value = afterVal - preVal;
            if(value.isNaN())
                value = 0.0;

            //return the diff
            return value;

        } catch (Exception e){
            Logger.error("Error in function: calculateDiff, feature number: " + featureNumber + ", example: " + example.path);
            return 0;
        }
    }

    //calculate the average difference of featureNumber win_size before and after location
    public double calculateDiff(Example example, int win_size, int location, int featureNumberNumerator, int featureNumberDenominator)
    {
        try {
            double preVal;
            //get the cumulative values of featureNumberNumerator and featureNumberDenominator
            double startValNum = CacheVowelData.getCumulativeValue(example, location - win_size, featureNumberNumerator);
            double startValDen = CacheVowelData.getCumulativeValue(example, location - win_size, featureNumberDenominator);
            double endValNum = CacheVowelData.getCumulativeValue(example, location, featureNumberNumerator);
            double endValDen = CacheVowelData.getCumulativeValue(example, location, featureNumberDenominator);

            Double startVal = startValNum / startValDen;
            Double endVal = endValNum / endValDen;
            preVal = endVal - startVal;
            preVal /= win_size;

            double afterVal;
            //get the cumulative values of featureNumber
            startVal = endVal;
            endValNum = CacheVowelData.getCumulativeValue(example, location + win_size, featureNumberNumerator);
            endValDen = CacheVowelData.getCumulativeValue(example, location + win_size, featureNumberDenominator);
            endVal = endValNum / endValDen;
            afterVal = endVal - startVal;
            afterVal /= win_size;

            Double value = afterVal - preVal;
            if(value.isNaN())
                value = 0.0;

            //return the diff
            return value;

        } catch (Exception e){
            Logger.error("Error in function: calculateDiff, feature number numerator: " + featureNumberNumerator + ", feature number denominator: "+featureNumberDenominator+", example: " + example.path);
            return 0;
        }
    }

    //calculate the avg value from start till end of featureNumber
    //if isPrev equals true then create mean difference from start
    //else create mean difference from end
    public double calculateMean(Example example, int start, int end, int featureNumber, int win_size, boolean isPrev)
    {
        try {
            double avg;
            int counter = end - start;

            //get the cumulative values of featureNumber
            double startVal = CacheVowelData.getCumulativeValue(example,start,featureNumber);
            double endVal = CacheVowelData.getCumulativeValue(example,end,featureNumber);

            //computer the mean
            avg = endVal-startVal;

            avg /= counter;

            double val;
            if(isPrev) {
                //get the cumulative values of featureNumber
                startVal = CacheVowelData.getCumulativeValue(example, start - win_size, featureNumber);
                endVal = CacheVowelData.getCumulativeValue(example, start, featureNumber);

                val = endVal - startVal;
                val /= win_size;
            } else {
                //get the cumulative values of featureNumber
                startVal = CacheVowelData.getCumulativeValue(example, end, featureNumber);
                endVal = CacheVowelData.getCumulativeValue(example, end + win_size, featureNumber);

                val = startVal - endVal;
                val /= win_size;
            }

            Double value = MathHelpers.sigmoid(avg - val);
            if(value.isNaN())
                value = 0.0;

            return value;

        } catch (Exception e){
            Logger.error("Error in function: calculateMean, feature number: "+featureNumber+", example: "+example.path);
            return 0;
        }
    }

    public double calculateMeanNoDifference(Example example, int start, int end, int featureNumber)
    {
        double avg;
        int counter = end - start;

        //get the cumulative values of featureNumber
        double startVal = CacheVowelData.getCumulativeValue(example,start,featureNumber);
        double endVal = CacheVowelData.getCumulativeValue(example,end,featureNumber);

        //computer the mean
        avg = endVal-startVal;
        avg /= counter;

        return avg;
    }

    //calculate the variance from start till end of featureNumber
    //if isPrev equals true then create variance difference from start
    //else create variance difference from end
    public double calculateVariance(Example example, int start, int end, int featureNumber, int win_size, boolean isPrev)
    {
        try{
            double mean = calculateMeanNoDifference(example, start, end, featureNumber);
            double var = 0;

            int counter = 0;
            for (int i = start; i < end; i++) {
                var += Math.pow(example.getFeatures2D().get(i).get(featureNumber) - mean, 2);
                counter++;
            }
            var /= counter;

            if (isPrev) {
                double mean_prev = calculateMeanNoDifference(example, start - win_size, start, featureNumber);
                double var_prev = 0;
                int counter_prev = 0;
                for (int i = start - win_size; i < start; i++) {
                    var_prev += Math.pow(example.getFeatures2D().get(i).get(featureNumber) - mean_prev, 2);
                    counter_prev++;
                }
                var_prev /= counter_prev;
                return var - var_prev;
            } else {
                double mean_prevStart = calculateMeanNoDifference(example, end, end + win_size, featureNumber);
                double var_prevStart = 0;
                int counter_prev = 0;
                for (int i = end; i < end + win_size; i++) {
                    var_prevStart += Math.pow(example.getFeatures2D().get(i).get(featureNumber) - mean_prevStart, 2);
                    counter_prev++;
                }
                var_prevStart /= counter_prev;
                return var - var_prevStart;
            }
        } catch (Exception e){
            Logger.error("Exception in calculate variance, "+example.path+", "+featureNumber);
            e.printStackTrace();
            return 0;
        }
    }

    //calculate the average difference between each two samples from start to end
    public double calculateInnerDifference(Example example, int start, int end, int featureNumber, int win_size, boolean isPrev) throws Exception
    {
        try {
            Double avgDiff = 0.0;
            int counter = 0;

            for (int i = start + 1; i < end; i++) {
                avgDiff += Math.abs(example.getFeatures2D().get(i).get(featureNumber) - example.getFeatures2D().get(i - 1).get(featureNumber));
                counter++;
            }
            avgDiff /= counter;

            if (isPrev) {
                Double avgDiff_prev = 0.0;
                int counter_prev = 0;
                for (int i = start - win_size + 1; i < start; i++) {
                    avgDiff_prev += Math.abs(example.getFeatures2D().get(i).get(featureNumber) - example.getFeatures2D().get(i - 1).get(featureNumber));
                    counter_prev++;
                }
                avgDiff_prev /= counter_prev;
                return (avgDiff - avgDiff_prev);
            } else {
                Double avgDiff_prev = 0.0;
                int counter_prev = 0;
                for (int i = end + 1; i < end + win_size; i++) {
                    avgDiff_prev += Math.abs(example.getFeatures2D().get(i).get(featureNumber) - example.getFeatures2D().get(i - 1).get(featureNumber));
                    counter_prev++;
                }
                avgDiff_prev /= counter_prev;
                return (avgDiff - avgDiff_prev);
            }
        } catch (Exception e){
            Logger.error("Exception in calculate Inner difference, "+example.path+", "+featureNumber);
            e.printStackTrace();
            return 0;
        }
    }

    //finds the max value from start to end of feature number
    public double findMax(Example example, int start, int end, int featureNumber) throws Exception
    {
        double max = 0;
        boolean isFirst = true;

        for(int i=start ; i<end ; i++) {
            if(isFirst){
                max = example.getFeatures2D().get(i).get(featureNumber);
                isFirst = false;
                continue;
            }
            if(max < example.getFeatures2D().get(i).get(featureNumber))
                max = example.getFeatures2D().get(i).get(featureNumber);
        }
        return max;
    }
    //******************************************************************************************//
    //******************************************************************************************//
}
