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

package com.structed.constants;

import java.util.HashMap;

/**
 * Created by yossiadi on 20/12/2015.
 *
 */
public class Char2Idx {
    final static public HashMap<Character, Integer> char2id = new HashMap<Character, Integer>(){
        {put('$', 0);}{put('a', 1);}{put('b', 2);}{put('c', 3);}{put('d', 4);}{put('e', 5);}
        {put('f', 6);}{put('g', 7);}{put('h', 8);}{put('i', 9);}{put('j', 10);}{put('k', 11);}
        {put('l', 12);}{put('m', 13);}{put('n', 14);}{put('o', 15);}{put('p', 16);}{put('q', 17);}
        {put('r', 18);}{put('s', 19);}{put('t', 20);}{put('u', 21);}{put('v', 22);}{put('w', 23);}{put('x', 24);}
        {put('y', 25);}{put('z', 26);}
    };
    final static public HashMap<Integer, Character> id2char = new HashMap<Integer, Character>(){
        {put(0, '$');}{put(1, 'a');}{put(2, 'b');}{put(3, 'c');}{put(4, 'd');}{put(5, 'e');}
        {put(6, 'f');}{put(7, 'g');}{put(8, 'h');}{put(9, 'i');}{put(10, 'j');}{put(11, 'k');}
        {put(12, 'l');}{put(13, 'm');}{put(14, 'n');}{put(15, 'o');}{put(16, 'p');}{put(17, 'q');}
        {put(18, 'r');}{put(19, 's');}{put(20, 't');}{put(21, 'u');}{put(22, 'v');}{put(23, 'w');}{put(24, 'x');}
        {put(25, 'y');}{put(26, 'z');}
    };
}
