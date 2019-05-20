# stack-overflow-miner-404
A second iteration of a small program designed to strip data about specific tags and words found in stack overflow posts as provided by the archives

# Instructions for filtering architecture posts
- Load the project into your IDE
- Make sure app class runs 
    - parser.parseForReduction("in.txt", "out.txt", Tags.ARCHITECTURE);
- Run the project to generate target folder
- add an in.txt file with all posts to filter
- posts with tag architecture will be saved to out.txt file
- This may take a long time, especially with larger input files


# Instructions for analysis
- Load the project into your IDE
- Make sure the app class runs 
    - parser.parseForAnalysis("in.txt", "analysis.txt");
- Run the project to generate target folder
- add an in.txt file with all architecture posts in the target folder
- the analysis will be written to analysis.txt file in target folder