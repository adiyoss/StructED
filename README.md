README
======

StructED 1.0.0

Copyright (C) Adiyoss

StructED is a collection of machine learning algorithms for structured prediction. Structured tasks are distinctive: each task has a unique evaluation metric, its own set of feature functions, and in many cases a non-standard IInference. Consequently, implementation of a machine learning system that utilizes structured prediction is complex and involved. The package handles this inherent complexity by introducing specially-designed interfaces for an evaluation function, a set of feature functions, and a decoder. The collection of all the training algorithms is implemented using the same interfaces, and adding a new training algorithm is straightforward. The library is written in Java, hence is platform independent. It is available at http://adiyoss.github.io/StructED/.

Keywords: structured prediction, structured SVM, CRF, direct loss minimization, structured ramp loss, structured probit Loss, structured passive aggressive