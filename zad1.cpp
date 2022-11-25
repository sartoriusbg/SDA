//#include<iostream>
//#include<string>
//#include<vector>
//#include<cmath>
//const int MAX_RANGE = 1000000;
//using namespace std;
//void coutArr(int* numbers,size_t lenght)
//{
//    for (size_t i = 0; i < lenght; i++)
//    {
//        cout << numbers[i] << " ";
//    }
//}
//void coutVector(vector<int> numbers)
//{
//    if (numbers.size() != 0)
//    {
//        for (size_t i = 0; i < numbers.size(); i++)
//        {
//            cout << numbers[i] << " ";
//        }
//    }
//}
//void solution(string placement,char version)
//{
//   vector<int> versionPlaces;
//   for(size_t i=0;i<placement.size();i++)
//   {
//       if (placement[i] == version)
//       {
//           versionPlaces.push_back(i);
//       }
//   }
//   vector<int> solution;
//   for (size_t j = 0; j < placement.size(); j++)
//   {
//       solution.push_back(MAX_RANGE);
//   }
//   
//   for (int k = 0; k < versionPlaces.size(); k++)
//   {
//       for (int j = 0; j < placement.size(); j++)
//       {
//           if (solution[j] > abs(j - int(versionPlaces[k])))
//           {
//               solution[j] = abs(j - int(versionPlaces[k]));
//           }
//       }
//   }
//   coutVector(solution);
//}
//int main()
//{
//   
//    string placement;
//    char variant;
//    cin >> placement;
//    cin >> variant;
//    solution(placement, variant);
//    return 0;
//}