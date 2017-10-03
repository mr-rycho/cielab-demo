This little module demonstrates the usefulness of the `cielab2000` color space and the color difference formula. This demo
presents the internals of the `ColorPickerLab` class that utilizes the aforementioned algorithm.

The problem is as follows: we want to pick a set of `n` distinct colors that are as different as possible. These colors
will be used to paint a graph of n functions over time (so the colors must be "far" away from each other because no one
wants to overstrain their eyes trying to distinguish if it is light gray or a little bit lighter gray).

The `ColorPickerLab` picks `n` colors iteratively:
* pick `n-1` colors that are "far" away from each other
* pick random `k` colors (`k` constant or a function of `n`)
* for each of the `k` colors count `minDist(c) = min(dist(c, col(1)), dist(c, col(2)), ..., dist(c, col(n-1)))`
* for `n`-th color pick a color `c` with highest `minDist(c)`

To see how this algorithm is doing take a look at `outl.png`. On the left side we have already picked `n-1` colors (`n=11`).
On the bottom there are `k` candidates (`k=10`) for the `n`-th color. Inside the matrix there are color distances:
in every cell there are two numbers:
* the number on the top is the distance calculated by euclidian in yuv space
* the number below is the distance calculated by the _cie2000_ method

The underlined numbers are `minDist`s.
For many cases both algorithms pick the same color.
Both numbers also appear on the bottom colored line. The underlined number mark which color was picked
as the `n`-th color by each algorithm. You can see that the first algo picked the light-pink color because it is
at least `0.0463` from every of `n-1` colors. However it is noticable that it is similar to the a-bit-darker pink
(position 2 from the top). The _cielab2000_ algorithm picked the dark green color because its `minDist` is `26.1630` .

The lab stuff taken from https://github.com/StanfordHCI/c3/blob/master/java/src/edu/stanford/vis/color/LAB.java .