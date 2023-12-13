package de.delusions.aoc;

import de.delusions.util.Day;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day12 extends Day<Integer> {

    static Pattern SPRING_REG = Pattern.compile( "([#\\?]+)" );

    static Pattern NUMBER_REG = Pattern.compile( "(\\d+)" );

    public Day12( Integer... expected ) {
        super( 12, "Hot Springs", expected );
    }


    static int folding = 1;

    @Override
    public Integer part0( Stream<String> input ) {
        folding = 1;
        List<SpringRow> rows = input.map( SpringRow::create ).toList();
        return countAllRowConfigurations( rows );
    }

    @Override
    public Integer part1( Stream<String> input ) {
        folding = 5;
        List<SpringRow> rows = input.map( SpringRow::create ).toList();
        return countAllRowConfigurations( rows );
    }

    static int countAllRowConfigurations( List<SpringRow> rows ) {
        int configurations = 0;
        for ( SpringRow row : rows ) {
            int rowConfigs = getConfigurations( row );
            configurations += rowConfigs;
            System.out.println( row + "configurations=" + rowConfigs );
        }
        return configurations;
    }

    static int getConfigurations( SpringRow row ) {
        Stack<String> opens = new Stack<>();
        opens.push( row.original() );
        Set<String> candidates = new HashSet<>();
        while ( !opens.isEmpty() ) {
            String next = opens.pop();
            if ( next.indexOf( '?' ) < 0 ) {
                if ( row.matches( next ) ) {
                    candidates.add( next );
                }
            }
            else {
                opens.push( next.replaceFirst( "\\?", "#" ) );
                opens.push( next.replaceFirst( "\\?", "." ) );
            }
        }
        return candidates.size();
    }

    private static List<String> createGroups( String line ) {
        Matcher matcher = SPRING_REG.matcher( line );
        List<String> groups = new ArrayList<>();
        while ( matcher.find() ) {
            groups.add( matcher.group( 1 ) );
        }
        return groups;
    }

    record SpringRow(String original, List<String> groups, List<Integer> brokenGroups) {
        static SpringRow create( String line ) {
            List<String> groups = createGroups( line );
            Matcher matcher = NUMBER_REG.matcher( line );
            List<Integer> broken = new ArrayList<>();
            while ( matcher.find() ) {
                broken.add( Integer.parseInt( matcher.group( 1 ) ) );
            }
            String original = line.split( " " )[0];
            List<Integer> foldedBroken = new ArrayList<>();
            StringBuilder foldedOriginal = new StringBuilder();
            for ( int i = 0; i < folding; i++ ) {
                foldedOriginal.append( original );
                foldedBroken.addAll( broken );
            }
            return new SpringRow( foldedOriginal.toString(), groups, foldedBroken );
        }

        boolean matches( String candidate ) {
            return brokenGroups().equals( createGroups( candidate ).stream().map( String::length ).toList() );
        }

    }


}
