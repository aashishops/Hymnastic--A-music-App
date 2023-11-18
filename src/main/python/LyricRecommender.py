import mysql.connector
import pandas as pd
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
import warnings
warnings.filterwarnings('ignore')



connection = mysql.connector.connect(
    host="localhost",
    user="root",
    password="root",
    database="hymnastic",
    auth_plugin='mysql_native_password'
)


query = "SELECT * FROM lyric"
lyric_df = pd.read_sql(query, con=connection)

# Close the connection
connection.close()

import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

import sys

# Check if the correct number of arguments is provided
if len(sys.argv) != 2:
    print("Usage: python ArtistRecommender.py <lyric>")
    sys.exit(1)

lyric = sys.argv[1]

def recommend_songs(input_lyrics, lyric_df =lyric_df, num_recommendations=5):
    # Create a new DataFrame with the input lyrics
    new_row = pd.DataFrame({'artist': ['Input'], 'song': ['Input Song'], 'link': [''], 'text': [input_lyrics]})
    lyric_df = pd.concat([lyric_df, new_row], ignore_index=True)

    # Use TfidfVectorizer to convert text data into numerical features
    vectorizer = TfidfVectorizer(stop_words='english')
    tfidf_matrix = vectorizer.fit_transform(lyric_df['text'])

    # Calculate cosine similarity between input lyrics and all other lyrics
    cosine_similarities = cosine_similarity(tfidf_matrix[-1], tfidf_matrix[:-1])

    # Get the indices of the top N most similar songs
    recommended_song_indices = cosine_similarities.argsort()[0][-num_recommendations:][::-1]

    # Get the details of the recommended songs
    recommended_songs = lyric_df.iloc[recommended_song_indices]

    # Return the recommended songs details as a list of tuples
    recommendations = [(row['artist'], row['title']) for _, row in recommended_songs.iterrows()]
    return recommendations

# # Example usage:
# input_lyrics = "nigga"
# # Assuming lyric_df is your DataFrame

# # Call the recommendation function
recommendations = recommend_songs(lyric)

# Print the recommended songs
for i, (artist, song) in enumerate(recommendations, 1):
    print(f"{song} by {artist}1")
