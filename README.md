README
======

StructED 1.0.0

Copyright (C) Adiyoss

StructED is a collection of machine learning algorithms for structured prediction problems. In structured prediction each task is distinctive and has its own set of feature functions, unique measure of performance and in many cases a non-standard inference. StructED package was designed to handle this inherent implementation complexity by having an easy interface to user-defined feature functions, decoder and evaluation function. The package is a general framework for implementation structured prediction problems and algorithms, and it is highly customizable. All algorithms are implemented by the same interfaces, so adding new algorithms or comparing them is straightforward. The library is written in Java, hence platform independent, and is available at http://adiyoss.github.io/StructED/ 
Keywords: Machine Learning, Structured Prediction, SVM, Direct Loss, CRF, Ramp Loss, Probit Loss, Passive Aggressive, RankSVM


HOW TO USE
======
All the source code of structed is availabe to dounload from this reposetory. To use StructED in your own project you should down load the code and implement three interfaces that are task dependent to your problem, the TaskLoss interface with is responsiable for the loss/cost function, the Prediction interface which is responsiable for the inferences (argmax, argmax+loss) and the PhiConvertor interface which is responsiable for the feature functions/feature maps.

You can find very detaild tutorial about adding new task to StructED under the docs/ directory.

CONFIG FILE - TRAIN
======

All the parameters in the config file should be as follows: 

parameter type, colon(:), parameter value

GENERAL PARAMETERS
=====
	
 - train_path: the path to the training set data - Mandatory 
 - w_output: output file of the weights vector (model)	
 - type: the algorithm type - Mandatory
 	- 0 - Passive Aggressive
 	- 1 - SVM
 	- 2 - Direct Loss
 	- 3 - CRF
 	- 4 - Ramp Loss
 	- 5 - Probit Loss
 - task: the cost/loss function number - Mandatory
 - epoch: the number of epochs on the data - Mandatory
 - task_param: cost/loss parameters if needed, can store multiple parameters splited by (;) - optional
 - kernel: kernel type, and parameters(i.e sigma) - Optional
 	- 0 - poly 2 degree
	- 1 - 2nd taylor approximation for RBF - setting the sigma value can be done like that: kernel:1:19
	- 2 - 3nd taylor approximation for RBF - setting the sigma value can be done like that: kernel:2:19
 - init_w: the path for the initial weights - Optional
 - phi: feature function type - Mandatory
 - prediction: prediction function, should implement also the loss-augmented function - Mandatory
 - reader: db reader type - Mandatory
 - writer: db writer type - Mandatory
	- for both reader and writer
		- use 0 for standard form
 - isAvg: boolean that indicates whether to average the weights vector - optional, default is 0
 - size_of_vector: size of vector after the feature mapping functions - Mandatory

SPECIFIC - ALGORITHM DEPENDENT
===

Passive Aggressive
===

 - c: 				 C parameter for the PA algorithm				

SVM
===

 - lambda:				 lambda parameter for the SVM						
 - eta:				 	eta - learning rate
			    

Direct Loss
===

 - eta:				 learning rate
 - epsilon:			 epsilon parameter for the DL	   
						  

CRF	
===

 - eta:				 	learning rate
 - lambda:				lambda parameter for the CRF

Ramp Loss	
===

 - eta:				 	learning rate
 - lambda:				 lambda parameter for the RL				  			

Probit Loss
===

 - eta:				 learning rate
 - lambda:				 lambda parameter for the PL	
 - num_of_iteration:		 number of iteration for generation noise			  




CONFIG FILE - TEST
======

 - test_path:							 the path to the test set data - Mandatory 

 - output_file:							 the output file for the scores - Mandatory 

 - w_path:								 the weights vector(model) - Mandatory 

 - examples_2_display:						 how many examples to display in the scores file - Mandatory

 - task:								 the loss(cost) function, the same as the train - Mandatory

 - kernel:								 kernel type, and parameters(i.e sigma) - Optional
 								 0 - poly 2 degree
								 1 - RBF 2nd taylor approximation
								 2 - RBF 3nd taylor approximation
	
 - phi:								 feature function type - Mandatory

 - prediction:							 prediction function, should implement also the loss-augmented function - Mandatory

 - reader:								 reader type - Mandatory
 - writer:								 writer type - Mandatory
 								 for both reader and writer
								 use 0 for standard form
								 use 1 for standard rank form (as Letor 3.0)

 - size_of_vector:							 size of vector after the feature mapping functions - Mandatory


Examples
========
Inside StructED package you can find implementations to three tasks:
 - Dummy data
 - MNIST dataset (multi-class problem)
 - Vowel Duration Measurement

For each task we supplied all the relevent classes, task-loss, prediction and feature functions, all of them can be found under the src/ directory.
