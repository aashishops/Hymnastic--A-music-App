import pandas as pd
import mysql.connector
from sklearn.neighbors import NearestNeighbors
from sklearn.preprocessing import StandardScaler
import warnings
warnings.filterwarnings('ignore')
import logging
import json

# Configure logging

#logger = logging.get#logger(__name__)

# ...

# Establish database connection
connection = mysql.connector.connect(
    host="localhost",
    user="root",
    password="root",
    database="hymnastic",
    auth_plugin='mysql_native_password'
)

# Read data from the database
query = "SELECT * FROM songs"
songs_df = pd.read_sql(query, con=connection)

# Close the database connection
connection.close()

# Log the loaded data
#logger.debug("Loaded data from the database:\n%s", songs_df)

# Assuming you have a DataFrame named 'df' with the necessary columns
columns_to_use = ['popularity', 'danceability', 'energy', 'key', 'loudness', 'mode', 'speechiness', 'acousticness', 'instrumentalness', 'liveness', 'valence', 'tempo', 'time_signature']
# Standardize Data
scaler = StandardScaler()
songs_df['track_genre'] = songs_df['track_genre'].str.strip()

# Log the standardized data
#logger.debug("Standardized data:\n%s", songs_df)

import sys

# Check if the correct number of arguments is provided
if len(sys.argv) != 4:
    print("Usage: python SongRecommender.py <songName> <artistName> <numRecommendations>")
    sys.exit(1)

# Extracting command-line arguments
songName = sys.argv[1]
artistName = sys.argv[2]
numRecommendations = int(sys.argv[3])

# Rest of your script...

def recommend_song(song_name, artists, num_recommendations=5):
    # Convert num_recommendations to an integer
    num_recommendations = int(num_recommendations)

    # Log input parameters
    #logger.info(f"Recommendation request for song: {song_name}, artist: {artists}, num_recommendations: {num_recommendations}")

    # Filter the DataFrame based on the song and artist
    sub_df = songs_df[(songs_df['track_name'] == song_name) & (songs_df['artists'] == artists)]

    if sub_df.empty:
        #logger.warning("Song not found in the dataset.")
        return {"error": "Song not found in the dataset."}

    # Extract all unique genres for the given song
    song_genre = set(sub_df['track_genre'])

    # Create a sub-DataFrame with all genres for the given song
    sub_df_all_genres = songs_df[(songs_df['track_genre']).isin(song_genre)]

    # Check the number of samples in the subset
    num_samples = len(sub_df_all_genres)

    # Adjust n_neighbors based on the number of samples
    k = min(num_recommendations, num_samples)  # Set to the smaller of specified recommendations or the number of samples

    # Standardize the features for the subset
    df_subset_scaled = scaler.fit_transform(sub_df_all_genres[columns_to_use])

    # Fit KNN Model on the subset
    knn_model = NearestNeighbors(n_neighbors=num_samples, algorithm='auto')
    knn_model.fit(df_subset_scaled)

    # Use the features of the specified song for recommendation
    song_features_scaled = scaler.transform(sub_df[columns_to_use])
    _, indices = knn_model.kneighbors(song_features_scaled)

    # Get the recommended songs based on indices
    recommended_songs = sub_df_all_genres.iloc[indices[0]][['track_name', 'artists', 'track_genre']]

    # Exclude the original song from the recommendations
    recommended_songs = recommended_songs[recommended_songs['track_name'] != song_name]

    # Ensure a fixed number of recommendations
    recommended_songs = recommended_songs.head(num_recommendations)

    # Filter out duplicate track names and artists
    recommended_songs = recommended_songs.drop_duplicates(subset=['track_name', 'artists'])

    # If the number of recommendations is less than the specified, fill with additional recommendations
    if len(recommended_songs) < num_recommendations:
        remaining_recommendations = songs_df[songs_df['track_name'] != song_name].sample(n=num_recommendations - len(recommended_songs))
        recommended_songs = pd.concat([recommended_songs, remaining_recommendations])

    # Create a new DataFrame with only song name and artist name
    simplified_recommendations = recommended_songs[['track_name', 'artists']].copy()

    # Convert the DataFrame to a JSON format
    recommendations_json = simplified_recommendations.to_dict(orient='records')

    # Log the recommendations
    #logger.info(f"Recommendations: {recommendations_json}")
    

    return recommendations_json

recommendations = recommend_song(songName, artistName, numRecommendations)

# Print the recommendations in the desired format
for recommendation in recommendations:
    print(f"\n{recommendation['track_name']} by {recommendation['artists']}1")

# # # Example usage
# song_name_to_search = 'Comedy'
# artist_name_to_search = 'Gen Hoshino'
# num_recommendations = 5  # Specify the number of recommendations as an integer

# recommendations = recommend_song(song_name_to_search, artist_name_to_search, num_recommendations)
# print(recommendations)
