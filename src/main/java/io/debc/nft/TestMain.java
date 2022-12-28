package io.debc.nft;

import io.debc.nft.contract.Erc721Contract;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-12 13:25
 **/
public class TestMain {

    public static void main(String[] args) throws Exception {


        //System.out.println(new Erc20Contract().name("0xbb9bc244d798123fde783fcc1c72d3bb8c189413"));
        Erc721Contract erc721Contract = new Erc721Contract();
        String s = erc721Contract.ownerOf("0xba30E5F9Bb24caa003E9f2f0497Ad287FDF95623", "2406");

        System.out.println(s);

    }


}
