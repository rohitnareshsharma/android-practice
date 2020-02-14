package com.practice.data.models;

/**
 * Model representing individual contributor of repo. Its respective json mapping is given below.
 *
 * {
 *   "href": "https://github.com/viatsko",
 *   "avatar": "https://avatars0.githubusercontent.com/u/376065",
 *   "username": "viatsko"
 * }
 *
 */
public class Contributor extends Model {

    public String href;

    public String avatar;

    public String username;
}
