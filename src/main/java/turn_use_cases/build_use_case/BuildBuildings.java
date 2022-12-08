package turn_use_cases.build_use_case;

import game_entities.Board;
import game_entities.Player;
import game_entities.tiles.ColorPropertyTile;
import game_entities.tiles.Property;

import java.util.ArrayList;


/**
 * All methods related to build buildings.
 */

public class BuildBuildings implements BuildBuildingInputBoundary{

    private final BuildBuildingOutputBoundary presenter;
    private final Board board;

    public BuildBuildings(BuildBuildingOutputBoundary presenter, Board board) {
        this.presenter = presenter;
        this.board = board;
    }

    /**
     * Check if player can build buildings
     *
     * @param player the player who wants  to build buildings.
     * @param property the property on which player wants to build buildings.
     */
    @Override
    public boolean isBuildable (Player player, Property property){
        if(!player.ownsProperty(property)){
            return false;
        }
        if(property.isMortgaged()){
            return false;
        }
        if(!(property instanceof ColorPropertyTile)){
            return false;
        }
        ColorPropertyTile colorProperty = (ColorPropertyTile) property;
//        System.out.println(board.getPropertyTiles());
        if(!colorProperty.checkSetOwned(board.getPropertyTiles())) {
            return false;
        }
        ArrayList<Property> properties = player.getProperties();
        ArrayList<ColorPropertyTile> colorProperties = new ArrayList<>();
        for(int i = 0; i < properties.size(); i++){
            if((properties.get(i) instanceof ColorPropertyTile)
                    && ((ColorPropertyTile) properties.get(i)).getColor().equals(colorProperty.getColor())){
                colorProperties.add((ColorPropertyTile) properties.get(i));
            }
        }
        for (int i = 0; i < colorProperties.size(); i++) {
            int a = colorProperties.get(i).getNumHouses() + colorProperties.get(i).getNumHotels();
            int b = colorProperty.getNumHouses() + colorProperty.getNumHotels();
            if (b > a) {
                return false;
            }
        }
        // Selecting ColorPropertyTile from player's properties.
        //ArrayList<ColorPropertyTile> colorProperties = new ArrayList<>();
        //ArrayList<Property> properties = player.getProperties();
        //for (int i = 0; i < properties.size(); i++){
        //if(properties.get(i) instanceof ColorPropertyTile){
        //colorProperties.add((ColorPropertyTile) properties.get(i));
        //}
        //}
        // Selecting ColorPropertyTile with the same color.
        //String color = colorProperty.getColor();
        //ArrayList<ColorPropertyTile> sameColorProperties = new ArrayList<>();
        //for (int i = 0; i < colorProperties.size(); i++){
        //if(colorProperties.get(i).getColor().equals(color)) {
        //sameColorProperties.add(colorProperties.get(i));
        //}
        //}
        //Player has to build equally – this means player can’t build a second house on a property unless player has a house on all the other properties.
        return true;
    }

    /**
     * A list of properties on which can build a building.
     *
     * @param player the player who wants to build buildings.
     */
    @Override
    public void showBuildOption (Player player){
        ArrayList<Property> properties = player.getProperties();
        ArrayList<ColorPropertyTile> buildOptions = new ArrayList<>();
        for (int i = 0; i < properties.size(); i++){
//            System.out.println(properties.get(i));
            if(isBuildable(player,properties.get(i))){
                buildOptions.add((ColorPropertyTile) properties.get(i));
            }
        }
        String text = "This is a list of properties you can build a building.";
        presenter.showBuildOption(buildOptions, text);
    }




    /**
     * Build a house on the property.
     *
     * @param player the player who wants to build buildings.
     * @param property the property on which player wants to build buildings.
     */
    @Override
    public void buildHouse (Player player, ColorPropertyTile property){
        if (isBuildable(player, property)){
            property.addHouse(1);
            //need to add a method addHouse in ColorPropertyTile class to add the number of house.
            String color = property.getColor();
            player.subtractMoney(property.getBuildingCost());
            String text = player.getName() + "built a house on " + property.getTileName();
            presenter.showBuildBuilding(player, property, text);
        }
        String text = player.getName() + "cannot build a house on " + property.getTileName();
        presenter.showBuildBuilding(player, property, text);
    }

    /**
     * Build a hotel on the property.
     *
     * @param player the player who wants to build buildings.
     * @param property the property on which player wants to build buildings.
     */
    @Override
    public void buildHotel (Player player, ColorPropertyTile property){
        if (isBuildable(player, property) && property.getNumHouses() >= 4){
            property.addHotel(1);
            //need to add a method addHotel in ColorPropertyTile class to add the number of hotel.
            String color = property.getColor();
            player.subtractMoney(property.getBuildingCost());
            String text = player.getName() + "built a hotel on " + property.getTileName();
            presenter.showBuildBuilding(player, property, text);
        }
        String text = player.getName() + "cannot build a hotel on " + property.getTileName();
        presenter.showBuildBuilding(player, property, text);
    }

    /**
     * Check if player can sell buildings
     *
     * @param player the player who wants to sell buildings.
     * @param property the property on which player wants to sell buildings.
     */
    @Override
    public boolean isSellable(Player player, ColorPropertyTile property) {
        if(!player.ownsProperty(property)){
            return false;
        }
        if(property.getNumHouses() == 0){
            return false;
        }
        ArrayList<ColorPropertyTile> ColorProperties = new ArrayList<ColorPropertyTile>();
        ArrayList<Property> properties = player.getProperties();
        for (int i = 0; i < properties.size(); i++){
            if(properties.get(i) instanceof ColorPropertyTile){
                ColorProperties.add((ColorPropertyTile) properties.get(i));
            }
        }
        // Selecting ColorPropertyTile with the same color.
        String color = property.getColor();
        ArrayList<ColorPropertyTile> SameColorProperties = new ArrayList<ColorPropertyTile>();
        for (int i = 0; i < ColorProperties.size(); i++){
            if(ColorProperties.get(i).getColor().equals(color)) {
                SameColorProperties.add(ColorProperties.get(i));
            }
        }
        for (ColorPropertyTile sameColorProperty : SameColorProperties) {
            int a = sameColorProperty.getNumHouses() + sameColorProperty.getNumHotels();
            int b = property.getNumHouses() + property.getNumHotels();
            if (b < a) {
                return false;
            }
        }
        return true;
    }

    /**
     * A list of properties which has building can be sold.
     *
     * @param player the player who wants to sell buildings.
     */
    @Override
    public void showSellOption (Player player){
        ArrayList<Property> properties = player.getProperties();
        ArrayList<ColorPropertyTile> sellOptions = new ArrayList<>();
        for (int i = 0; i < properties.size(); i++){
            if( properties.get(i) instanceof ColorPropertyTile && isSellable(player, (ColorPropertyTile) properties.get(i))){
                sellOptions.add((ColorPropertyTile) properties.get(i));
            }
        }
        String text = "This is a list of properties you can sell a building.";
        presenter.showSellOption(sellOptions, text);
    }

    /**
     * Selling a house on the property.
     *
     * @param player the player who wants to sell buildings.
     * @param property the property on which player wants to sell buildings.
     */
    @Override
    public void sellHouse(Player player, ColorPropertyTile property){
        if (isSellable(player, property) && property.getNumHotels() == 0){
            property.subtractHouse(1);
            int value = 0;
            value = (int) (0.5*property.getBuildingCost());
            player.addMoney(value);
            String text = player.getName() + "sold a house on " + property.getTileName();
            presenter.showSellBuilding(player, property, text);
        }
        String text = player.getName() + "cannot sell a house on " + property.getTileName();
        presenter.showSellBuilding(player, property, text);
    }

    /**
     * Selling a hotel on the property.
     *
     * @param player the player who wants to sell buildings.
     * @param property the property on which player wants to sell buildings.
     */
    @Override
    public void sellHotel(Player player, ColorPropertyTile property){
        if (isSellable(player, property) && property.getNumHotels() > 0){
            property.subtractHotel(1);
            int value = 0;
            value = (int) (0.5*property.getBuildingCost());
            player.addMoney(value);
            String text = player.getName() + "sold a hotel on " + property.getTileName();
            presenter.showSellBuilding(player, property, text);
        }
        String text = player.getName() + "cannot sell a hotel on " + property.getTileName();
        presenter.showSellBuilding(player, property, text);
    }
}
