The data-set used is the ‘Cocktails’ data-set from 2020.
https://raw.githubusercontent.com/rfordatascience/tidytuesday/master/data/2020/2020-05-26/cocktails.csv
https://raw.githubusercontent.com/rfordatascience/tidytuesday/master/data/2020/2020-05-26/boston_cocktails.csv
As format of some of the data from ‘boston_cocktails.csv’ were different from the general format of the other data, 
such as rather than putting each ingredient in separate rows, 
those with same measurement being put into a single row or the unit of the measurement in different formats, 
preprocessing of data cleaning had been done before being used for plotting. 
For those rows were multiple ingredients have been placed in the same row, 
the rows were separated so that each row would represent only one ingredient and in case of units not in oz, 
the units were converted into oz; 
‘1 750-ml’ to ‘25.4 oz’, ‘2 750-ml’ to ‘50.8 oz’, ‘splash’ to ‘0.5 oz’, ‘1’ for egg to ‘0.75 oz’, ‘Juice of Lemon or Lime’ to ‘1.5 oz’, ‘2 bottles’ to ‘50 oz’. 
After doing so, extra column, abv, alcohol by volume (%), has been added to the dataset where each of the ingredient’s abv has been added as follows.

> 5%: Beer, Cider, Strout
> 10%: Aperol
> 12%: Port, Sherry, Wine, Champagne
> 15%: Cynar, Claret, Punt e Mes
> 20%: Vermouth, Schnapps, Curacao, Triple Sec, Creme types, Amaretto, Liqueuer types, Dubonnet (Rouge), Lillet (Blanc/Rouge), Sake, Amaro, Madeira, Campari, Swedish Punch
> 25%: Anisette, Cherry Heering
> 30%: Maraschino, Limoncello
> 40%: Rum/Whiskey/Brandy/Gin/Vodka/Cognac/Tequila types, Bitters, Benedictine, Kummel, Fernet-Branca, Grand Marnier, Pimm’s No. 1 Cup, Kirschwasser, B & B, Strega, Aquavit, Calvados, Pisco, Sambuca, Forbidden Fruit, Becherovka, Drambuie
> 45%: Applejack, Absinthe, Galliano, Cachaca
> 50%: 100 proof vodka
> 55%: Chartreuse, Mezcal, Armagnac
> 75.5%: 151-Proof Rum
For the first graph, ABV (Alcohol by Volume) of Cocktails, 
it is targeted towards a layman interested in cocktails. Unlike other beverages, 
cocktails are made from mixing various liquors, making it hard to determine how strong the drink is or it’s alcohol content. 
Not knowing the estimate amount of alcohol in the drink, there would have been cases where when trying out new cocktails, 
one couldn’t finish it because it was too strong to handle or perhaps was too weak and did not feel like drinking alcohol at all. 
Repeated failure of cocktail picks may turn one to become reluctant in trying out other new cocktails. 
Hence, the following graph is to aid individuals in getting grasp of the rough abv of the cocktail drinks and make cocktail choices relatively easier, 
where the abv has been calculated by

1. determining abv of all the ingredients
2. calculate the alcohol oz of each ingredients: (abv x oz)/100 where abv is given as %
3. for each cocktail drink, find abv(%): (sum of alcohol oz of the ingredients)/(sum of oz of all the liquid ingredients) x 100
However, again, this would merely be a rough estimate as depending on the choice of liquors or the recipe, the abv would vary. 
Therefore, when creating the plot, I have plotted only the name of the cocktails and jittered the position with width 0.5 and height 0.3 as the main goal of this plot lies in only providing guideline of rough estimate of the alcohol content so plotting only the name of the cocktails would allow easier visualization of it’s corresponding abv while allowing for minimum overlap between the plotted geoms. 
For the same reason, text geoms have been plotted using geom_text_repel with the line segment, connecting the point and the text, being transparent. 
Another thing to note is that as the number of cocktails given were too many to be plotted as names into a single graph, only the cocktails appearing in both the ‘cocktails.csv’ and ‘boston_cocktails.csv’ were plotted with the assumption that a cocktail appearing in both files would be more common at bars so that the below plot could actually come in handy in more general terms. 
From the names of cocktails both appearing in ‘cocktails.csv’ and ‘boston_cocktails.csv’, as ‘Bloody Marry’ was the only data with too large of the total volume of the drink, it has been removed to make the plot be more compact. 
Along with it’s abv, the total volume of the drink and the category in which it lies is also given. 
Yet, as they are provided only as a reference, the axis text of x is removed and the size and the title for legend is reduced. 
<img width="733" alt="Screen Shot 2023-03-26 at 10 22 52 PM" src="https://user-images.githubusercontent.com/99862881/227782245-a091d457-041f-4ccc-adc3-07a897a42475.png">

The second graph is targeted towards more specific audience, those with basic understanding of statistics and has interest in cocktails as well. 
The second graph shows the distribution of boston cocktails’ abv distribution by category. 
Again, the abv is a rough estimate and this graph merely shows overall distribution and brief summary of the cocktails by the categories. 
For easier recognition of the distribution, the transparency of the points is set to 0.2 with position of the points jittered by height 0.1 and width 0.5. 
Here, while the plots have been colour-coded by categories to make it more visually appealing, as the colours do not have much meaningful attribution to the plot with the y-axis already representing the categories, 
the legend has been removed. 
Even so, as the plots are still being too clustered to make any meaningful insights at a glance, 
the stat_summary abv of each category is added in black for the corresponding category to show what the general distribution of abv of each category of drinks would be. 
From this, it can be seen that boston cocktails in whiskey category would have higher abv compared to those in vodka category in general.
<img width="550" alt="Screen Shot 2023-03-26 at 10 23 07 PM" src="https://user-images.githubusercontent.com/99862881/227782253-c0a19334-be8c-4aac-a09f-d40d72afc7fc.png">


