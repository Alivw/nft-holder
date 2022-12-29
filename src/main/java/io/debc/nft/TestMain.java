package io.debc.nft;

import com.esaulpaugh.headlong.abi.Tuple;
import com.esaulpaugh.headlong.abi.TupleType;
import com.esaulpaugh.headlong.util.FastHex;
import io.debc.nft.contract.Erc1155Contract;
import io.debc.nft.contract.Erc721Contract;
import io.debc.nft.utils.SysUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-12 13:25
 **/
public class TestMain {

    public static void main(String[] args) throws Exception {


        List<Integer> ans = new ArrayList<>();

        add(ans);

        System.out.println(ans.size());
        System.out.println();
    }

    private static void add(List<Integer> ans) {
        ans.add(1);
        ans.add(2);
    }


}
