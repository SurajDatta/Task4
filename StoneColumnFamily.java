/*
 * Copyright 2017 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.Sample.blocks;


import gnu.trove.map.hash.TByteObjectHashMap;
import org.terasology.math.Side;
import org.terasology.math.SideBitFlag;
import org.terasology.math.geom.Vector3i;;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockBuilderHelper;
import org.terasology.world.block.BlockUri;
import org.terasology.world.block.family.BlockFamily;
import org.terasology.world.block.family.BlockSections;
import org.terasology.world.block.family.RegisterBlockFamily;
import org.terasology.world.block.loader.BlockFamilyDefinition;
import org.terasology.world.block.shapes.BlockShape;


import org.terasology.world.block.family.MultiConnectFamily;

import static org.terasology.math.SideBitFlag.*;


@RegisterBlockFamily("sample:StoneColumn")
@BlockSections({"bottom", "top", "middle"})
public class StoneColumnFamily extends MultiConnectFamily {

    public StoneColumnFamily(BlockFamilyDefinition definition, BlockShape shape, BlockBuilderHelper blockBuilder) {
        super(definition, shape, blockBuilder);
    }

    BlockUri blockUri;


    public StoneColumnFamily(BlockFamilyDefinition definition, BlockBuilderHelper blockBuilder) {
        super(definition, blockBuilder);

        blocks = new TByteObjectHashMap<Block>();

        blockUri = new BlockUri(definition.getUrn());


        getSide(Side.TOP), "bottom", definition, blockBuilder;
        getSide(Side.BOTTOM), "top", definition, blockBuilder;
        getSides(Side.TOP, Side.BOTTOM), "middle", definition, blockBuilder;

        this.setBlockUri(setBlockUri());
        this.setCategory(definition.getCategories());
    }


    protected boolean connectionCondition(Vector3i blockLocation, Side connectSide) {
        Vector3i neighborLocation = new Vector3i(blockLocation);
        neighborLocation.add(connectSide.getVector3i());
        if (worldProvider.isBlockRelevant(neighborLocation)) {
            Block neighborBlock = worldProvider.getBlock(neighborLocation);
            final BlockFamily blockFamily = neighborBlock.getBlockFamily();
            if (blockFamily instanceof RomanColumnFamily) {
                return true;
            }
        }
        return false;
    }

    @Override
    public byte getConnectionSides() {
        return getSides(Side.TOP, Side.BOTTOM);
    }

    @Override
    public Block getArchetypeBlock() {
        return blocks.get(getSides(Side.RIGHT, Side.LEFT));
    }

}


