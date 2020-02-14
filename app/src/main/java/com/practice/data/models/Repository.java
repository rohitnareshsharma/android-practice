package com.practice.data.models;

import java.util.List;

/**
 * Model representing trending repo object. Its respective json mapping is given below.
 *
 *   {
 *     "author": "kusti8",
 *     "name": "proton-native",
 *     "avatar": "https://github.com/kusti8.png",
 *     "url": "https://github.com/kusti8/proton-native",
 *     "description": "A React environment for cross platform native desktop apps",
 *     "language": "JavaScript",
 *     "languageColor": "#3572A5",
 *     "stars": 4711,
 *     "forks": 124,
 *     "currentPeriodStars": 1186,
 *     "builtBy": [
 *       {
 *         "href": "https://github.com/viatsko",
 *         "avatar": "https://avatars0.githubusercontent.com/u/376065",
 *         "username": "viatsko"
 *       }
 *     ]
 *   }
 *
 */
public class Repository extends Model {

    public String author;

    public String name;

    public String avatar;

    public String url;

    public String description;

    public String stars;

    public String forks;

    public String language;

    public String languageColor;

    public String currentPeriodStars;

    public List<Contributor> builtBy;

}
