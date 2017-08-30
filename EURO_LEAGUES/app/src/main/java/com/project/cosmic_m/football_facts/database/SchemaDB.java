package com.project.cosmic_m.football_facts.database;

/**
 * Created by Cosmic_M on 23.07.2017.
 */

public class SchemaDB {

    public static final class LeagueTable{
        public static final String NAME = "leagueTable";
        public static final class Cols{
            public static final String CAPTION = "caption";
            public static final String LEAGUE = "league";//compressed caption
            public static final String CAPTION_ID = "captionId";
            public static final String QUANTITY_TEAMS = "quantityTeams";
        }
    }//public static final class LeagueTable

    public static final class TeamStandingTable{
        public static final String NAME = "teamStandingTable";
        public static final class Cols {
            public static final String LEAGUE_CAPTION = "leagueCaption";
            public static final String MATCH_DAY = "matchDay";
            public static final class InnerCols {
                public static final String TEAM = "teamName";
                public static final String RANK = "rank";
                public static final String TEAM_ID = "teamId";
                public static final String PLAYED_GAMES = "playedGames";
                public static final String WINED_GAMES = "winedGames";
                public static final String DRAWN_GAMES = "drawnGames";
                public static final String LOSSES_GAMES = "lossesGames";
                public static final String POINTS = "points";
                public static final String GOALS = "goals";
                public static final String GOALS_AGAINST = "goalsAgainst";
                public static final String GOALS_DIFFERENCE = "goalsDifference";
                public static final String RESOURCE_IMAGE = "resourceOfPicture";
                public static final String FIRST_LAST_RESULT = "firstLastResult";
                public static final String SECOND_LAST_RESULT = "secondLastResult";
                public static final String THIRD_LAST_RESULT = "thirdLastResult";
                public static final String FOURTH_LAST_RESULT = "fourthLastResult";
                public static final String FIFTH_LAST_RESULT = "fifthLastResult";
                public static final String GROUP = "groupOfChampions";
            }
        }
    }//public static final class TableTeamStanding

    public static final class EventTable{
        public static final String NAME = "eventTable";
        public static final class Cols{
            public static final String MATCH_ID = "matchId";
            public static final String COMPETITION_ID = "competitionId";
            public static final String DATE = "matchDate";
            public static final String MATCH_DAY = "matchDay";
            public static final String HOME_TEAM_NAME = "homeTeamName";
            public static final String HOME_TEAM_ID = "homeTeamId";
            public static final String AWAY_TEAM_NAME = "awayTeamName";
            public static final String AWAY_TEAM_ID = "awayTeamId";
            public static final String STATUS = "status";
            public static final String GOALS_HOME_TEAM = "goalsHomeTeam";
            public static final String GOALS_AWAY_TEAM = "goalsAwayTeam";
        }
    }//public static final class EventTable
}