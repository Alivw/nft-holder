package io.debc.nft.thread.entity.thread;

import java.util.Arrays;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-12 13:25
 **/
public class TestMain {

    public static void main(String[] args) throws Exception {


        //System.out.println(new Erc20Contract().name("0xbb9bc244d798123fde783fcc1c72d3bb8c189413"));
        String[] split = "192.168.31.55,192.168.31.55".split(",");
        Arrays.stream(split).forEach(e-> System.out.println("qq"+e));

    }


}
