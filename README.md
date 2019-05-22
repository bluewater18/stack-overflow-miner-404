# stack-overflow-miner-404
A second iteration of a small program designed to strip data about specific tags and words found in stack overflow posts as provided by the archives

# Instructions for filtering architecture posts
- Build the jar with dependencies
- Include the posts to be filtered as "in.txt" in the jar directory
- Run "java stack-overflow-miner-1.0-jar-with-dependencies.jar -jar reduce"
- The filtered posts will be saved to "filtered.txt" in the jar directory


# Instructions for analysis
<ul> 
    <li>Build the jar with dependencies</li>
    <li>Filtering and analysis:</li>
    <ul>
        <li>Include the posts to be filtered as "in.txt" in the jar directory</li>
        <li>Run "java stack-overflow-miner-1.0-jar-with-dependencies.jar -jar reduce-analyse 20" (where 20 is the tag graph threshold)</li>
        <li>The filtered posts will be saved to "filtered.txt" in the jar directory</li>
        <li>The analysis will be saved to "analysis.txt"</li>
        <li>4 graphs will be created (architecture.png, framework.png, language.png, tags.png)</li>
    </ul>
    <li>Analysis only:</li>
    <ul> 
        <li>Inlcude posts to analyse as "filtered.txt" in the jar directory</li>
        <li>Run "java stack-overflow-miner-1.0-jar-with-dependencies.jar -jar analyse 20 (where 20 is the tag graph threshold)</li>
    </ul>
    <li>The analysis will be saved to "analysis.txt"</li>
    <li>4 graphs will be created (architecture.png, framework.png, language.png, tags.png)</li>
</ul>
