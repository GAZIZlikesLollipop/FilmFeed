package com.app.filmfeed

import com.app.filmfeed.data.MovieCategories
import com.app.filmfeed.data.MovieItem
import com.app.filmfeed.data.MovieMember

fun getMovs(): List<MovieItem> {
    val members = listOf(
        MovieMember(
            id = 0,
            name = "Bred Pitt",
            featuredFilms = listOf(),
            photo = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2Fthumb%2F4%2F4c%2FBrad_Pitt_2019_by_Glenn_Francis.jpg%2F1200px-Brad_Pitt_2019_by_Glenn_Francis.jpg&f=1&nofb=1&ipt=2ff5d7bd43cb445808d9aeb11f93dab5cd1f45058c99e9044dec070e36eb25bb.jpg",
            roles = listOf("Actor"),
            character = "Tyler Durden",
            birthDate = "18.12.1963"
        ),
        MovieMember(
            id = 1,
            name = "Devid Fincher",
            featuredFilms = listOf(),
            photo = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fcdn.britannica.com%2F20%2F222620-050-FF919598%2FMovie-director-David-Fincher-2018.jpg&f=1&nofb=1&ipt=dbd282a9daa965e672482fbb61462caddb5a19e9a082b8260da55d869d32d050",
            roles = listOf("Director"),
            birthDate = "28.08.1962"
        ),
        MovieMember(
            id = 2,
            name = "Matthew McConaughey",
            featuredFilms = listOf(),
            photo = "https://upload.wikimedia.org/wikipedia/commons/thumb/b/bf/Matthew_McConaughey_2019_%2848648344772%29.jpg/500px-Matthew_McConaughey_2019_%2848648344772%29.jpg",
            roles = listOf("Actor"),
            character = "Cooper",
            birthDate = "04.11.1969"
        ),
        MovieMember(
            id = 3,
            name = "Christopher Nolan",
            featuredFilms = listOf(),
            photo = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/49/ChrisNolanBFI150224_%2810_of_12%29_%2853532289710%29_%28cropped2%29.jpg/500px-ChrisNolanBFI150224_%2810_of_12%29_%2853532289710%29_%28cropped2%29.jpg",
            roles = listOf("Director"),
            birthDate = "30.07.1970"
        ),
        MovieMember(
            id = 4,
            name = "Leonardo DiCaprio",
            featuredFilms = listOf(),
            photo = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/46/Leonardo_Dicaprio_Cannes_2019.jpg/500px-Leonardo_Dicaprio_Cannes_2019.jpg",
            roles = listOf("Actor"),
            character = "Jack Dawson",
            birthDate = "11.11.1974"
        ),
        MovieMember(
            id = 5,
            name = "James Cameron",
            featuredFilms = listOf(),
            photo = "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c0/Avatar_The_Way_of_Water_Tokyo_Press_Conference_James_Cameron_%2852563430565%29_%28cropped%29.jpg/500px-Avatar_The_Way_of_Water_Tokyo_Press_Conference_James_Cameron_%2852563430565%29_%28cropped%29.jpg",
            roles = listOf("Director"),
            birthDate = "16.08.1954"
        ),
    )

    val films = listOf(
        MovieItem(
            id = 0,
            name = "Fight Club",
            posterURL = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.themoviedb.org%2Ft%2Fp%2Foriginal%2FiqR0M1ln7Kobjp9liUj2Q7mtQZG.jpg&f=1&nofb=1&ipt=de9c89ba87992157c40bc1ad2a956e3e020ede961bf7497b73e6fda29c1781fa",
            movieURL = "https://ia800304.us.archive.org/11/items/fight-club_202404/Fight%20Club.mp4",
            categories = listOf(MovieCategories.Drama, MovieCategories.Thriller),
            rating = 8.7,
            reviews = 1133103,
            description = "An insurance company employee suffers from chronic insomnia and is desperate to escape his excruciatingly boring life. One day, on a business trip, he meets a certain Tyler Durden, a charismatic soap salesman with a twisted philosophy. Tyler is sure that self-improvement is for the weak, and the only thing worth living for is self-destruction.\n\n It doesn't take long before the new friends are beating each other up in the parking lot of a bar, and the cathartic fistfights give them the highest bliss. Introducing other men to the simple joys of physical brutality, they start a secret Fight Club that becomes incredibly popular.",
            country = "USA",
            year = 1999,
            members = listOf(members[0], members[1]),
            duration = 8400,
            age = 18
        ),
        MovieItem(
            id = 1,
            name = "Interstellar",
            posterURL = "https://i.ebayimg.com/00/s/MTYwMFgxMDY2/z/zu4AAOSw2spbJQ0J/\$_57.JPG?set_id=8800005007.jpg",
            movieURL = "https://dn721908.ca.archive.org/0/items/interstellar-2014_202409/Interstellar%282014%29.mp4",
            categories = listOf(
                MovieCategories.Drama,
                MovieCategories.Fantasy,
                MovieCategories.Adventure
            ),
            rating = 8.7,
            reviews = 1066169,
            description = "When drought, dust storms, and plant extinction leave humanity with a food crisis, a team of explorers and scientists travel through a wormhole (which supposedly connects regions of space-time across vast distances) on a journey to surpass previous limitations of human space travel and find a planet with suitable conditions for humanity.",
            country = "USA",
            year = 2014,
            members = listOf(members[3], members[2]),
            duration = 10200,
            age = 18
        ),
        MovieItem(
            id = 2,
            name = "Titanic",
            posterURL = "https://m.media-amazon.com/images/I/610CYrdV7AS.jpg",
            movieURL = "https://ia601802.us.archive.org/35/items/titanic-1996-full-movie-1080p-6-ynd-9gu-9-zvk/Titanic%201996%20Full%20movie%201080p-6YNd9gu9ZVk.mp4",
            categories = listOf(
                MovieCategories.Drama,
                MovieCategories.Thriller,
                MovieCategories.History,
            ),
            rating = 8.4,
            reviews = 908214,
            description = "A story of love, tragedy, and human resilience set against the historical disaster of the RMS Titanic, a luxury ocean liner that sank in 1912. The film follows the passionate romance between Jack (Leonardo DiCaprio), a free-spirited artist, and Rose (Kate Winslet), a wealthy socialite, as they navigate the tensions of class, love, and survival. As the ship drifts toward disaster, their bond is tested by the chaos of the night, the cold sea, and the ultimate sacrifice of those who dared to defy the fate of the iceberg. The film is a sweeping tale of hope, heartbreak, and the enduring power of love in the face of death.",
            country = "USA",
            year = 1997,
            members = listOf(members[4], members[5]),
            duration = 11700,
            age = 12
        )
    )
    return films
}