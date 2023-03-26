import random
import sys

# Helper functions to aid in your implementation. Can edit/remove
#############################################################################
######## Piece
#############################################################################
class Piece:
    def __init__(self, position):
        self.position = position
        self.x = position[0]
        self.y = position[1]

    def threat(self, rows, cols, obs, occupied):
        can_threat = set()
        can_threat.add((self.x, self.y))

        for move in self.moves():
            x_cord = self.x + move[0]
            y_cord = self.y + move[1]
            while (x_cord in range(rows)) and (y_cord in range(cols)):
                if (x_cord, y_cord) in obs:
                    break

                elif (x_cord, y_cord) in occupied:
                    return False

                else:
                    can_threat.add((x_cord, y_cord))

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
    def threat(self, rows, cols, obs, occupied):
        as_knight = Knight(self.position).threat(rows, cols, obs, occupied)
        if as_knight == False:
            return False
        else:
            out = Bishop(self.position).threat(rows, cols, obs, occupied)
            if out == False:
                return out
            else:
                return out.union(as_knight)

class Empress(Piece):
    def threat(self, rows, cols, obs, occupied):
        as_knight = Knight(self.position).threat(rows, cols, obs, occupied)
        if as_knight == False:
            return False
        else:
            out = Rook(self.position).threat(rows, cols, obs, occupied)
            if out == False:
                return out
            else:
                return out.union(as_knight)

def initialize_piece(key, coord, rows, cols, obs, occupied):
    pieceInitialize = {
        6: King(coord),
        0: Queen(coord),
        3: Bishop(coord),
        4: Rook(coord),
        5: Knight(coord),
        7: Ferz(coord),
        2: Princess(coord),
        1: Empress(coord)
    }
    return pieceInitialize[key].threat(rows, cols, obs, occupied)

def backtrack(pieces_to_add, rows, cols, output, ith, aval, obs, occupied):
    if len(pieces_to_add) == ith:
        return output

    if len(pieces_to_add) - ith > len(aval):
        return None

    piece = pieces_to_add[ith]
    for coord in aval:
        added = initialize_piece(piece, coord, rows, cols, obs, occupied)
        if added != False:
            output[conv_coord(coord)] = conv_piece(piece)
            occupied.add(coord)
            new_aval = aval - added
            next_iter = backtrack(pieces_to_add, rows, cols, output, ith+1, new_aval, obs, occupied)

            if next_iter != None:
                return next_iter
            del output[conv_coord(coord)]
            occupied.remove(coord)

def conv_coord(coord):
    return (chr(coord[1]+97), coord[0])

def conv_piece(piece):
    pieceDic = {6: 'King',
                0: 'Queen',
                3: 'Bishop',
                4: 'Rook',
                5: 'Knight',
                7: 'Ferz',
                2: 'Princess',
                1: 'Empress'}
    return pieceDic[piece]

#############################################################################
######## Implement Search Algorithm
#############################################################################
def search(rows, cols, grid, num_pieces, obs):
    # Prioritize pieces by the number of grid it can threat
    priority = {0: 6, # King
                1: 0, # Queen
                2: 3, # Bishop
                3: 4, # Rook
                4: 5, # Knight
                5: 7, # Ferz
                6: 2, # Princess
                7: 1, # Empress
                }
    # Create list of chess pieces that need to be added
    pieces_to_add = []
    for i in range(len(num_pieces)):
        for j in range(num_pieces[i]):
            pieces_to_add.append(priority[i])

    pieces_to_add.sort()

    # list of pieces to add, rows, cols, output dic, num of pieces added, aval loc, obs loc, occupied
    return backtrack(pieces_to_add, rows, cols, {}, 0, grid, obs, set())


#############################################################################
######## Parser function and helper functions
#############################################################################
### DO NOT EDIT/REMOVE THE FUNCTION BELOW###
def parse(testcase):
    handle = open(testcase, "r")

    get_par = lambda x: x.split(":")[1]
    rows = int(get_par(handle.readline()))
    cols = int(get_par(handle.readline()))
    grid = set()
    for i in range(rows):
        for j in range(cols):
            grid.add((i,j))
    obs = set()

    num_obstacles = int(get_par(handle.readline()))
    if num_obstacles > 0:
        for ch_coord in get_par(handle.readline()).split():  # Init obstacles
            r, c = from_chess_coord(ch_coord)
            obs.add((r, c))
            grid.remove((r, c))
    else:
        handle.readline()
    
    piece_nums = get_par(handle.readline()).split()
    num_pieces = [int(x) for x in piece_nums] #List in the order of King, Queen, Bishop, Rook, Knight

    return rows, cols, grid, num_pieces, obs

def add_piece( comma_seperated):
    piece, ch_coord = comma_seperated.split(",")
    r, c = from_chess_coord(ch_coord)
    return [(r,c), piece]

#Returns row and col index in integers respectively
def from_chess_coord( ch_coord):
    return (int(ch_coord[1:]), ord(ch_coord[0]) - 97)

### DO NOT EDIT/REMOVE THE FUNCTION HEADER BELOW###
# To return: Goal State which is a dictionary containing a mapping of the position of the grid to the chess piece type.
# Chess Pieces (String): King, Queen, Knight, Bishop, Rook (First letter capitalized)
# Positions: Tuple. (column (String format), row (Int)). Example: ('a', 0)

# Goal State to return example: {('a', 0) : Queen, ('d', 10) : Knight, ('g', 25) : Rook}
def run_CSP():
    testcase = sys.argv[1] #Do not remove. This is your input testfile.
    rows, cols, grid, num_pieces, obs = parse(testcase)
    goalstate = search(rows, cols, grid, num_pieces, obs)
    return goalstate #Format to be returned
