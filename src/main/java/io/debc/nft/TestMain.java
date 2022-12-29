package io.debc.nft;

import com.esaulpaugh.headlong.abi.Tuple;
import com.esaulpaugh.headlong.abi.TupleType;
import com.esaulpaugh.headlong.util.FastHex;
import io.debc.nft.contract.Erc1155Contract;
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
        Erc1155Contract contract = new Erc1155Contract();
        System.out.println(new BigInteger("11").toString(16));
        System.out.println(contract.supportInterface("0xfbd41e0c4517a684c121aa7dda2fc462d9592e63"));
        System.out.println();
    }


}
