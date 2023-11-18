import mysql.connector
import pandas as pd
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


query = "SELECT * FROM songs"
songs_df = pd.read_sql(query, con=connection)

# Close the connection
connection.close()

columns_to_drop = ['sno', 'track_id', 'album_name', 'track_name', 'key', 'mode','duration_ms','explicit','time_signature']

# Drop the specified columns
artists_df = songs_df.drop(columns=columns_to_drop, axis=1)
numeric_columns = artists_df.select_dtypes(include=['number']).columns
average_df = artists_df.groupby(['artists', 'track_genre'], as_index=False)[numeric_columns].mean()

# Optionally, you can add the count of each genre for an artist
genre_counts = artists_df.groupby(['artists', 'track_genre'], as_index=False).size().rename(columns={'size': 'genre_count'})

# Merge the two DataFrames on 'artists' and 'track_genre'
result_df = pd.merge(average_df, genre_counts, on=['artists', 'track_genre'])

import sys

# Check if the correct number of arguments is provided
if len(sys.argv) != 2:
    print("Usage: python ArtistRecommender.py <artistName>")
    sys.exit(1)

artistName = sys.argv[1]


def get_artist_recommendations(artist_name, result_df=result_df, num_recommendations=5, print_recommendations=True):
    # Filter the DataFrame to include only the rows for the given artist
    artist_data = result_df[result_df['artists'] == artist_name]

    if artist_data.empty:
        print(f"Artist '{artist_name}' not found.")
        return []

    # Extract numeric columns for similarity calculation
    numeric_data = artist_data[numeric_columns]
    
    # Extract the genres of the input artist
    input_genres = artist_data['track_genre'].tolist()

    # Filter the result_df to include only artists from the same genres
    sub_df = result_df[(result_df['track_genre']).isin(input_genres)]

    if sub_df.empty:
        print(f"No similar artists found in the same genre for '{artist_name}'.")
        return []

    # Calculate cosine similarity with other artists
    similarities = cosine_similarity(numeric_data, sub_df[numeric_columns])

    # Get indices of artists with the highest similarity
    similar_artist_indices = similarities.argsort()[0][::-1][1:]

    # Get recommended artists (limited to num_recommendations)
    recommended_artists = sub_df.iloc[similar_artist_indices[:num_recommendations]]['artists'].tolist()

  

    return recommended_artists

rec = get_artist_recommendations(artistName)

count_hashtags = 0

for reco in rec:
    artist_without_semicolon = reco.replace(';', '1')
    print(f"\n{artist_without_semicolon}1")
    
    # Count the number of '#' characters
    count_hashtags += artist_without_semicolon.count('1')

    # Stop printing when there are 5 '#' characters
    if count_hashtags >= 5:
        break

