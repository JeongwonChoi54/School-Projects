import operator
import sys
import random

# Helper functions to aid in your implementation. Can edit/remove
#############################################################################
######## Piece
#############################################################################

class Piece:
    def __init__(self, position):
        self.position = position
        self.x = position[0]
        self.y = position[1]

    def threat(self, grid, rows, cols, can_threat):
        for move in self.moves():
            x_cord = self.x + move[0]
            y_cord = self.y + move[1]
            pieces_between = set()

            while (x_cord in range(rows)) and (y_cord in range(cols)):
                if grid[x_cord][y_cord] == -1:
                    break

                elif grid[x_cord][y_cord] == 1:
                    can_threat[(x_cord, y_cord)] = pieces_between.copy()
                    pieces_between.add((x_cord, y_cord))

                    if self.repeat() == False:
                        break
                    else:
                        x_cord += move[0]
                        y_cord += move[1]
        return can_threat

class King(Piece):
    def moves(self):
        return [[-1, -1], [-1, 0], [-1, 1], [0, -1], [0, 1], [1, -1], [1, 0], [1, 1]]

    def repeat(self):
        return False

class Rook(Piece):
    def moves(self):
        return [[1, 0], [-1, 0], [0, 1], [0, -1]]

    def repeat(self):
        return True

class Bishop(Piece):
    def moves(self):
        return [[-1, -1], [-1, 1], [1, 1], [1, -1]]

    def repeat(self):
        return True

class Queen(Piece):
    def moves(self):
        return [[1, 0], [-1, 0], [0, 1], [0, -1], [-1, -1], [-1, 1], [1, 1], [1, -1]]

    def repeat(self):
        return True

class Knight(Piece):
    def moves(self):
        return [[-1, -2], [1, -2], [-2, -1], [2, -1], [-2, 1], [-1, 2], [1, 2], [2, 1]]

    def repeat(self):
        return False

class Ferz(Piece):
    def moves(self):
        return [[-1, -1], [-1, 1], [1, 1], [1, -1]]

    def repeat(self):
        return False

class Princess(Piece):
    def threat(self, grid, rows, cols, can_threat):
        as_knight = Knight(self.position).threat(grid, rows, cols, can_threat)
        return Bishop(self.position).threat(grid, rows, cols, as_knight)

class Empress(Piece):
    def threat(self, grid, rows, cols, can_threat):
        as_knight = Knight(self.position).threat(grid, rows, cols, can_threat)
        return Rook(self.position).threat(grid, rows, cols, as_knight)

#############################################################################
######## Board
#############################################################################
class Board:
    def __init__(self, grid):
        self.grid = grid

    def piece_location(self, pieces, rows, cols):
        collisions = {}
        for val in pieces:
            piece = pieces[val]
            can_threat = getattr(globals()[piece](val), 'threat')(self.grid, rows, cols, {})
            collisions[val] = can_threat
        return self.grid, collisions


#############################################################################
######## State
#############################################################################
class State:
    def __init__(self, grid, rows, cols):
        self.grid = grid
        self.rows = rows
        self.cols = cols

def num_collision(collisions, cur_val, remove, blocking):
    new_collisions = {}

    for threatening in collisions.keys():
        # Remove num of threats posed by 'remove'
        if threatening == remove:
            for blockedby in collisions[threatening].values():
                if len(blockedby) == 0:
                    cur_val -= 1

        else:
            new_threats = {}
            for attacked in collisions[threatening].keys():
                # 'remove' can no longer be threatened
                if attacked == remove:
                    if len(collisions[threatening][attacked]) == 0:
                        cur_val -= 1
                else:
                    blockedby = collisions[threatening][attacked]
                    # 'remove' can no longer block
                    if remove in blocking.keys():
                        if threatening in blocking[remove]:
                            if attacked in blocking[remove][threatening]:
                                removal = set()
                                removal.add(remove)
                                blockedby = blockedby - removal
                                if len(blockedby) == 0:
                                    cur_val += 1
                    new_threats[attacked] = blockedby
            new_collisions[threatening] = new_threats

    return new_collisions, cur_val

def highest_successors(collisions, cur_num_collisions, blocking):
    next_cadidate = []
    lowest = cur_num_collisions

    for i in collisions.keys():
        cur_item = set()
        cur_item.add(i)
        new_collisions, num_collisions = num_collision(collisions, cur_num_collisions, i, blocking)
        if num_collisions < lowest:
            next_cadidate = []
            next_cadidate.append(new_collisions)
            lowest = num_collisions
        elif num_collisions == lowest:
            next_cadidate.append(new_collisions)

    return next_cadidate, lowest


def ans(pieces, cur_collisions):
    output = {}
    for key in cur_collisions.keys():
            output[(chr(key[1]+97), key[0])] = pieces[key]
    return output

#############################################################################
######## Implement Search Algorithm
#############################################################################
def search(rows, cols, grid, pieces, k):
    n = len(pieces)
    k = int(k)
    board = Board(grid)

    # collisions : Dict storing key as the piece and value as another dict
    #   value Dict stores the piece it threats as key and the pieces in between as val
    grid, collisions = board.piece_location(pieces, rows, cols)
    num_collisions = 0
    # blocking stores key of pieces and dict as val;
    #   dict stores 'a':'b' where the piece is currently blocking 'a' from threatening 'b'
    blocking = {}

    for a, b in collisions.items():
        for i, j in b.items():
            if len(j) == 0:
                num_collisions += 1
            else:
                for c in j:
                    blocking.setdefault(c, {})
                    blocking[c].setdefault(a, set())
                    x = blocking[c][a]
                    x.add(i)

    while True:
        # Random restart
        remove = random.sample(collisions.keys(), 1)[0]
        next_successor, next_num_collisions = num_collision(collisions, num_collisions, remove, blocking)
        if next_num_collisions == 0:
            return ans(pieces, next_successor)

        cur_num_collisions = next_num_collisions
        cur_collisions = next_successor

        num_pieces = n - 1
        while num_pieces >= k:
            next_successors, next_num_collisions = highest_successors(cur_collisions, cur_num_collisions, blocking)
            if next_num_collisions >= cur_num_collisions:
                return ans(pieces, cur_collisions)

            cur_num_collisions = next_num_collisions
            cur_collisions = random.choice(next_successors)
            num_pieces -= 1
#############################################################################
######## Parser function and helper functions
#############################################################################
### DO NOT EDIT/REMOVE THE FUNCTION BELOW###
def parse(testcase):
    handle = open(testcase, "r")

    get_par = lambda x: x.split(":")[1]
    rows = int(get_par(handle.readline()))
    cols = int(get_par(handle.readline()))
    grid = [[0 for j in range(cols)] for i in range(rows)]
    k = 0
    pieces = {}

    num_obstacles = int(get_par(handle.readline()))
    if num_obstacles > 0:
        for ch_coord in get_par(handle.readline()).split():  # Init obstacles
            r, c = from_chess_coord(ch_coord)
            grid[r][c] = -1
    else:
        handle.readline()

    k = handle.readline().split(":")[1]  # Read in value of k

    piece_nums = get_par(handle.readline()).split()
    num_pieces = 0
    for num in piece_nums:
        num_pieces += int(num)

    handle.readline()  # Ignore header
    for i in range(num_pieces):
        line = handle.readline()[1:-2]
        coords, piece = add_piece(line)
        pieces[coords] = piece
        grid[coords[0]][coords[1]] = 1

    return rows, cols, grid, pieces, k


def add_piece(comma_seperated):
    piece, ch_coord = comma_seperated.split(",")
    r, c = from_chess_coord(ch_coord)
    return [(r, c), piece]


# Returns row and col index in integers respectively
def from_chess_coord(ch_coord):
    return (int(ch_coord[1:]), ord(ch_coord[0]) - 97)


### DO NOT EDIT/REMOVE THE FUNCTION HEADER BELOW###
# To return: Goal State which is a dictionary containing a mapping of the position of the grid to the chess piece type.
# Chess Pieces (String): King, Queen, Knight, Bishop, Rook (First letter capitalized)
# Positions: Tuple. (column (String format), row (Int)). Example: ('a', 0)

# Goal State to return example: {('a', 0) : Queen, ('d', 10) : Knight, ('g', 25) : Rook}

def run_local():
    testcase = sys.argv[1]  # do not remove. this is your input testfile.
    rows, cols, grid, pieces, k = parse(testcase)
    goalstate = search(rows, cols, grid, pieces, k)
    return goalstate  # format to be returned
