package GameAssets;

public class Camera {
    private int x, y;
    private final int speed = 20;
    boolean locked = false;

    boolean dragged = false;
    int mouseX, mouseY;

    Game game;
    private int mapWidth, mapHeight;

    public void setup(Game game){
        this.game = game;
        mapWidth = game.map.getPresetMap().width;
        mapHeight = game.map.getPresetMap().height;
        this.x = 0;
        this.y = 0;
    }

    public void move(boolean up, boolean down, boolean left, boolean right){
        int h = 0, v = 0;
        if (up){v--;}
        if (down){v++;}
        if (left){h--;}
        if (right){h++;}

        move(h*speed, v*speed);
    }

    public void move(int horizontal, int vertical){
        if (this.x + horizontal > 0){
            if (this.x + horizontal < mapWidth){
                this.x += horizontal;
            } else {
                this.x = mapWidth;
            }
        } else {
            this.x = 0;
        }

        if (!(this.y + vertical < 0)){
            if (this.y + vertical < mapHeight){
                this.y += vertical;
            } else {
                this.y = mapHeight;
            }
        } else{
            this.y = 0;
        }
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void moveTo(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public void drag(int mouseX, int mouseY){
        if (!dragged){
            dragged = true;
        } else {
            move(this.mouseX - mouseX, this.mouseY - mouseY);
        }
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    public int anchorX(){
        return this.x - (ScreenConsts.WINDOWWIDTH/2);
    }
    public int anchorY(){
        return this.y - (ScreenConsts.WINDOWHEIGHT/2);
    }
    public int x(){
        return this.x;
    }
    public int y(){
        return this.y;
    }

    public boolean onScreen(int x, int y, int padding) {
        return this.x - (ScreenConsts.WINDOWWIDTH/2) - padding < x && x < this.x + (ScreenConsts.WINDOWWIDTH/2) + padding && this.y - (ScreenConsts.WINDOWHEIGHT/2) - padding < y && y < this.y + (ScreenConsts.WINDOWHEIGHT/2) + padding;
    }

}
