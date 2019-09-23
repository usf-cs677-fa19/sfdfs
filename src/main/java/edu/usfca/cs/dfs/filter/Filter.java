package edu.usfca.cs.dfs.filter;

public interface Filter {

    /**
     * add function takes a string adds it to a filter and returns the updated value at the position
     * @param str
     * @return
     */
    int add(String str);

    /**
     * check function takes in a string to check and returns true or false
     * @param str
     * @return
     */
    boolean check(String str);

    /**
     * delete function takes in a string to delete from filter and returns updated value at the position
     * @param str
     * @return
     */
    boolean delete(String str);
}
