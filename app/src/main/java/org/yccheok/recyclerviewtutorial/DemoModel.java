package org.yccheok.recyclerviewtutorial;

import java.util.Date;

/**
 * Created by yccheok on 17/11/2015.
 */
public class DemoModel {
    private static int nextId = 0;
    String label;
    Date dateTime;
    String pathToImage;
    int id = ++nextId;
}
