import sys
import functools
from queue import PriorityQueue
from queue import Queue

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
    def __init__(self, grid, own_pieces_loc, path, pathcost_g, pathcost_grid, heuristic, frontier, goals):
        self.grid = grid
        self.king_x = own_pieces_loc[0]
        self.king_y = own_pieces_loc[1]
        self.frontier = frontier
        self.path = path
        self.pathcost_g = pathcost_g
        self.pathcost_grid = pathcost_grid
        self.heuristic = heuristic
        self.goals = goals

    def move(self, rows, cols):
        for i in range(-1, 2):
            for j in range(-1, 2):
                if i == 0 and j == 0:
                    continue
                else:
                    x = self.king_x + i
                    y = self.king_y + j
                    if (x >= 0 and x < rows) and (y >= 0 and y < cols):
                        if self.grid[x][y] > 0:
                            if self.heuristic[x][y] == -1:
                                self.heuristic[x][y] = get_h(x, y, self.goals, self.grid)
                            new_pathcost_g = self.pathcost_g + self.grid[x][y]
                            if self.pathcost_grid[x][y] == 0 or self.pathcost_grid[x][y] > new_pathcost_g:
                                self.pathcost_grid[x][y] = new_pathcost_g
                                new_state = StateComp(State(self.grid, (x, y), self.path + [[(chr(self.king_y + 97), self.king_x), (chr(y+97), x)]],
                                                new_pathcost_g, self.pathcost_grid, self.heuristic, Queue(), self.goals))
                                self.frontier.put(new_state)
        return self.frontier

@functools.total_ordering
class StateComp(State):
    def __init__(self, state):
        self.grid = state.grid
        self.king_x = state.king_x
        self.king_y = state.king_y
        self.frontier = state.frontier
        self.path = state.path
        self.pathcost_g = state.pathcost_g
        self.pathcost_grid = state.pathcost_grid
        self.heuristic = state.heuristic
        self.goals = state.goals
        super().__init__(self.grid, (self.king_x, self.king_y), self.path, self.pathcost_g,
                         self.pathcost_grid, self.heuristic, self.frontier, self.goals)

    def __gt__(self, other):
        return (self.pathcost_g + self.heuristic[self.king_x][self.king_y]) > (other.pathcost_g + other.heuristic[other.king_x][other.king_y])

    def __lt__(self, other):
        return (self.pathcost_g + self.heuristic[self.king_x][self.king_y]) < (other.pathcost_g + other.heuristic[other.king_x][other.king_y])

    def __le__(self, other):
        return (self.pathcost_g + self.heuristic[self.king_x][self.king_y]) <= (other.pathcost_g + other.heuristic[other.king_x][other.king_y])

    def __ge__(self, other):
        return (self.pathcost_g + self.heuristic[self.king_x][self.king_y]) >= (other.pathcost_g + other.heuristic[other.king_x][other.king_y])

    def __eq__(self, other):
        return (self.pathcost_g + self.heuristic[self.king_x][self.king_y]) == (other.pathcost_g + other.heuristic[other.king_x][other.king_y])


#############################################################################
######## Implement Search Algorithm
#############################################################################
def search(rows, cols, grid, enemy_pieces, own_pieces, goals):
    board = Board(grid)
    grid = board.piece_location(enemy_pieces, own_pieces, rows, cols)

    finish = [[0 for j in range(cols)] for i in range(rows)]
    for (r, c) in goals:
        finish[r][c] = 1

    heuristic = [[-1 for j in range(cols)] for i in range(rows)]
    pathcost_grid = [[0 for j in range(cols)] for i in range(rows)]

    frontier = State(grid, own_pieces[0][1], [], 0, pathcost_grid, heuristic, PriorityQueue(), goals).move(rows, cols)

    while not frontier.empty():
        current = frontier.get()
        if finish[current.king_x][current.king_y] == 1:
            return current.path, current.pathcost_g
        successor = current.move(rows, cols)
        while not successor.empty():
            frontier.put(successor.get())
    return [], 0

def get_h(x, y, goals, grid):
    h_val = []
    for (r, c) in goals:
        if r==x and c==y:
            return 0
        h_val.append(abs(r - x) + abs(c - y) + grid[x][y] -1)
    return min(h_val)

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


### DO NOT EDIT/REMOVE THE FUNCTION HEADER BELOW###
# To return: List of moves and nodes explored
def run_AStar():
    testcase = sys.argv[1]
    rows, cols, grid, enemy_pieces, own_pieces, goals = parse(testcase)
    moves, pathcost = search(rows, cols, grid, enemy_pieces, own_pieces, goals)
    return moves, pathcost
