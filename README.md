README
======

StructED 1.0.0

Copyright (C) Adiyoss

StructED is a collection of machine learning algorithms for structured prediction. Structured tasks are distinctive: each task has a unique evaluation metric, its own set of feature functions, and in many cases a non-standard inference. Consequently, implementation of a machine learning system that utilizes structured prediction is complex and involved. The package handles this inherent complexity by introducing specially-designed interfaces for an evaluation function, a set of feature functions, and a decoder. The collection of all the training algorithms is implemented using the same interfaces, and adding a new training algorithm is straightforward. The library is written in Java, hence is platform independent. It is available at http://adiyoss.github.io/StructED/.

Keywords: structured prediction, structured SVM, CRF, direct loss minimization, structured ramp loss, structured probit Loss, structured passive aggressive


HOW TO USE
======
All the source code for StructED is available to download StructED repository. To use StructED in your own project you should download the code and implement three interfaces that are task dependent to your problem, the TaskLoss interface with is responsible for the loss/cost function, the Prediction interface which is responsible for the inferences (argmax, argmax+loss) and the PhiConvertor interface which is responsible for the feature functions/feature maps.

Very detailed tutorial about adding new task to StructED can be found under the docs/ directory.

CONFIG FILE - TRAIN
======

To run StructED (Train or Predict), one should supply a config file. The file should be from the following format: 
(All the parameters in the config file should be as follows: parameter type, colon(:), parameter value)
Examples of config files can be found under data/conf/algorithms_example directory

GENERAL PARAMETERS
=====
	
 - train_path: the path to the training set data - Mandatory 
 - w_output: the path to save the weights vector (model)	
 - type: the algorithm type - Mandatory
 	- 0 - Passive Aggressive
 	- 1 - SVM
 	- 2 - Direct Loss
 	- 3 - CRF
 	- 4 - Ramp Loss
 	- 5 - Probit Loss
	- 6 - Structured Perceptron
 - task: the cost/loss function number - Mandatory
 - epoch: the number of epochs on the data - Mandatory
 - task_param: cost/loss parameters if needed, can store multiple parameters splited by (;) - optional
 - kernel: kernel type, and parameters(i.e sigma) - Optional
 	- 0 - poly 2 degree
	- 1 - 2nd taylor approximation for RBF - setting the sigma value can be done like that: kernel:1:19
	- 2 - 3rd taylor approximation for RBF - setting the sigma value can be done like that: kernel:2:19
 - init_w: the path for the initial weights - Optional
 - phi: feature function type - Mandatory
 - prediction: prediction function, should implement also the loss-augmented function - Mandatory
 - reader: db reader type - Mandatory
 - writer: db writer type - Mandatory
	- for both reader and writer
		- use 0 for standard reader(like mnist db or the dummy data)
 - isAvg: boolean that indicates whether to average the weights vector - optional, default is 0
 - size_of_vector: size of vector after the feature mapping functions - Mandatory

SPECIFIC - ALGORITHM DEPENDENT
===

Passive Aggressive
===

 - c: parameter for the PA algorithm				

SVM
===

 - lambda: lambda parameter for the SVM						
 - eta: learning rate
			    

Direct Loss
===

 - eta: learning rate
 - epsilon: epsilon parameter for the DL	   
						  

CRF	
===

 - eta: learning rate
 - lambda: lambda parameter for the CRF

Ramp Loss	
===

 - eta:	learning rate
 - lambda: lambda parameter for the RL				  			

Probit Loss
===

 - eta:	learning rate
 - lambda: lambda parameter for the PL	
 - num_of_iteration: the number of times to generation noise for the weights vector
 - noise_all_vector: boolean(1/0) indicates whether to generate noise through all the weight vector or just in one random place
 - noise_mean: the mean of the noise to be generated(we draw the noise from a normal distribution)
 - noise_std: the standard deviation of the noise to be generated(we draw the noise from a normal distribution)

Structured Perceptron
===
 - The structured perceptron does not need any special parameters


CONFIG FILE - PREDICT
======

 - test_path: the path to the test set data - Mandatory 
 - output_file: the path to output the scores file - Mandatory
 - w_path: the path to the weights vector (model) - Mandatory 
 - examples_2_display: how many examples to display in the scores file - Mandatory
 - task: the loss(cost) function, the same as the train - Mandatory
 - task_param: task loss parameters if needed
 - kernel: kernel type, and parameters(i.e sigma) - Optional
 	- 0 - poly 2 degree
	- 1 - RBF 2nd taylor approximation - setting the sigma value can be done like that: kernel:1:19
	- 2 - RBF 3rd taylor approximation - setting the sigma value can be done like that: kernel:2:19
 - phi: feature function type - Mandatory
 - prediction: prediction function, should implement the inferences - Mandatory
 - reader: reader type - Mandatory
 - writer: writer type - Mandatory
	- for both reader and writer
		- use 0 for standard reader(like mnist db or the dummy data)
 - size_of_vector: size of vector after the feature mapping functions - Mandatory

Examples
========
Inside StructED package you can find implementations to three tasks:
 - Dummy data
 - MNIST dataset (multi-class problem)
 - Vowel Duration Measurement

For each task we supplied all the relevant classes, task-loss, prediction and feature functions, all of them can be found under the src/ directory.
