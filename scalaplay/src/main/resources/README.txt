Three Page Path - Load Times
----------------------------

The input files (input1_loadtimes.txt and input2_loadtimes.txt) represent
simplified access logs. The first column is a timestamp. The second column
is a customer ID. The third column is a page ID. The fourth column is the
page's load time, in milliseconds. Files are always ordered by timestamp.

Given a file like this, either a) calculate the most most common 3 page path
through the site, or b) determine the slowest loading 3 page path. This path is
across users. Users can visit a path multiple times. When they visit more than
three pages, you'll consider each consecutive pages of 3 as a separate path:
meaning, if they visit pages 1 - 4, there's 2 paths, 1-2-3 & 2-3-4.

If the first argument is "-p" then determine the most common 3 page path;
otherwise determine the slowest loading 3 page path. For the latter, output
the slowest loading path, it's slowest load time, and all users that visited
that path at least once (they don't need to all have experienced the slowest
load time - just that they visited the path that was slowest).
