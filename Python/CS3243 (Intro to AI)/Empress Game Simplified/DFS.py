import sys
import queue

# Helper functions to aid in your implementation. Can edit/remove
#############################################################################
######## Piece
#############################################################################
class Piece:
    def __init__(self, position):
        self.position = position
        self.x = position[0]
        self.y = position[1]

    def threat(self, grid, rows, cols):
        for move in self.moves():
            x_cord = self.x + move[0]
            y_cord = self.y + move[1]
            while (x_cord >= 0 and x_cord < rows) and (y_cord >= 0 and y_cord < cols):
                if grid[x_cord][y_cord] == -1:
                    break
                else:
                    grid[x_cord][y_cord] = 0
                    if self.repeat() == False:
                        break
                    else:
                        x_cord += move[0]
                        y_cord += move[1]
        return grid

class King(Piece):
    def __init__(self, position):
        super().__init__(position)

    def moves(self):
        return [[-1, -1], [-1, 0], [-1, 1], [0, -1], [0, 1], [1, -1], [1, 0], [1, 1]]

    def repeat(self):
        return False


class Rook(Piece):
    def __init__(self, position):
        super().__init__(position)

    def moves(self):
        return [[1, 0], [-1, 0], [0, 1], [0, -1]]

    def repeat(self):
        return True

class Bishop(Piece):
    def __init__(self, position):
        super().__init__(position)

    def moves(self):
        return [[-1, -1], [-1, 1], [1, 1], [1, -1]]

    def repeat(self):
        return True

class Queen(Piece):
    def __init__(self, position):
        super().__init__(position)

    def moves(self):
        return Rook(self.position).moves() + Bishop(self.position).moves()

    def repeat(self):
        return True

class Knight(Piece):
    def __init__(self, position):
        super().__init__(position)

    def moves(self):
        return [[-1, -2], [1, -2], [-2, -1], [2, -1], [-2, 1], [-1, 2], [1, 2], [2, 1]]

    def repeat(self):
        return False

class Ferz(Piece):
    def __init__(self, position):
        super().__init__(position)

    def moves(self):
        return [[-1, -1], [-1, 1], [1, 1], [1, -1]]

    def repeat(self):
        return False

class Princess(Piece):
    def __init__(self, position):
        super().__init__(position)

    def threat(self, grid, rows, cols):
        as_knight = Knight(self.position).threat(grid, rows, cols)
        return Bishop(self.position).threat(as_knight, rows, cols)

class Empress(Piece):
    def __init__(self, position):
        super().__init__(position)

    def threat(self, grid, rows, cols):
        as_knight = Knight(self.position).threat(grid, rows, cols)
        return Rook(self.position).threat(as_knight, rows, cols)

#############################################################################
######## Board
#############################################################################
class Board:
    def __init__(self, grid):
        self.grid = grid

    def modify_grid(self, x, y):
        self.grid[x][y] = -1
        return self.grid

    def piece_location(self, enemy_pieces, own_pieces, rows, cols):
        for enemy in enemy_pieces:
            self.modify_grid(enemy[1][0], enemy[1][1])
            self.grid = getattr(globals()[enemy[0]]((enemy[1][0], enemy[1][1])), 'threat')(self.grid, rows, cols)
        self.grid[own_pieces[0][1][0]][own_pieces[0][1][1]] = -1
        return self.grid


#############################################################################
######## State
#############################################################################
class State:
    def __init__(self, grid, own_pieces_loc, path, finish):
        self.grid = grid
        self.king_x = own_pieces_loc[0]
        self.king_y = own_pieces_loc[1]
        self.frontier = queue.LifoQueue()
        self.path = path
        self.finish = finish

    def get_grid(self):
        return self.grid

    def move(self, rows, cols):
        for i in range(-1, 2):
            for j in range(-1, 2):
                if i == 0 and j == 0:
                    continue
                else:
                    x = self.king_x + i
                    y = self.king_y + j
                    grid = self.get_grid()
                    grid[self.king_x][self.king_y] = -1
                    if (x >= 0 and x < rows) and (y >= 0 and y < cols):
                        if grid[x][y] > 0:
                            if self.finish[x][y] == 1:
                                return (1, self.path + [[(chr(self.king_y + 97), self.king_x), (chr(y+97), x)]])
                            new_state = State(grid, (x, y), self.path + [[(chr(self.king_y + 97), self.king_x), (chr(y+97), x)]], self.finish)
                            self.frontier.put(new_state)
        return self.frontier

#############################################################################
######## Implement Search Algorithm
#############################################################################
def search(rows, cols, grid, enemy_pieces, own_pieces, goals):
    board = Board(grid)
    grid = board.piece_location(enemy_pieces, own_pieces, rows, cols)
    finish = [[0 for j in range(cols)] for i in range(rows)]
    for (r, c) in goals:
        finish[r][c] = 1

    frontier = State(grid, own_pieces[0][1], [], finish).move(rows, cols)

    if type(frontier) is tuple:
        return frontier[1]

    while not frontier.empty():
        current = frontier.get()
        successor = current.move(rows, cols)
        if type(successor) is tuple:
            return successor[1]
        while not successor.empty():
            frontier.put(successor.get())
    return []

#############################################################################
######## Parser function and helper functions
#############################################################################
### DO NOT EDIT/REMOVE THE FUNCTION BELOW###
# Return number of rows, cols, grid containing obstacles and step costs of coordinates, enemy pieces, own piece, and goal positions
def parse(testcase):
    handle = open(testcase, "r")

    get_par = lambda x: x.split(":")[1]
    rows = int(get_par(handle.readline()))  # Integer
    cols = int(get_par(handle.readline()))  # Integer
    grid = [[1 for j in range(cols)] for i in range(rows)]  # Dictionary, label empty spaces as 1 (Default Step Cost)
    enemy_pieces = []  # List
    own_pieces = []  # List
    goals = []  # List

    handle.readline()  # Ignore number of obstacles
    for ch_coord in get_par(handle.readline()).split():  # Init obstacles
        r, c = from_chess_coord(ch_coord)
        grid[r][c] = -1  # Label Obstacle as -1

    handle.readline()  # Ignore Step Cost header
    line = handle.readline()
    while line.startswith("["):
        line = line[1:-2].split(",")
        r, c = from_chess_coord(line[0])
        grid[r][c] = int(line[1]) if grid[r][c] == 1 else grid[r][
            c]  # Reinitialize step cost for coordinates with different costs
        line = handle.readline()

    line = handle.readline()  # Read Enemy Position
    while line.startswith("["):
        line = line[1:-2]
        piece = add_piece(line)
        enemy_pieces.append(piece)
        line = handle.readline()

    # Read Own King Position
    line = handle.readline()[1:-2]
    piece = add_piece(line)
    own_pieces.append(piece)

    # Read Goal Positions
    for ch_coord in get_par(handle.readline()).split():
        r, c = from_chess_coord(ch_coord)
        goals.append((r, c))

    return rows, cols, grid, enemy_pieces, own_pieces, goals


def add_piece(comma_seperated) -> Piece:
    piece, ch_coord = comma_seperated.split(",")
    r, c = from_chess_coord(ch_coord)
    return [piece, (r, c)]


def from_chess_coord(ch_coord):
    return (int(ch_coord[1:]), ord(ch_coord[0]) - 97)


#############################################################################
######## Main function to be called
#############################################################################
### DO NOT EDIT/REMOVE THE FUNCTION BELOW###
# To return: List of moves
# Return Format Example: [[('a', 0), ('a', 1)], [('a', 1), ('c', 3)], [('c', 3), ('d', 5)]]
def run_BFS():
    testcase = sys.argv[1]
    rows, cols, grid, enemy_pieces, own_pieces, goals = parse(testcase)
    moves = search(rows, cols, grid, enemy_pieces, own_pieces, goals)
    return moves