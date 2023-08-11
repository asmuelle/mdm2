import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IScope } from '../scope.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../scope.test-samples';

import { ScopeService } from './scope.service';

const requireRestSample: IScope = {
  ...sampleWithRequiredData,
};

describe('Scope Service', () => {
  let service: ScopeService;
  let httpMock: HttpTestingController;
  let expectedResult: IScope | IScope[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ScopeService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Scope', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const scope = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(scope).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Scope', () => {
      const scope = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(scope).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Scope', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Scope', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Scope', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addScopeToCollectionIfMissing', () => {
      it('should add a Scope to an empty array', () => {
        const scope: IScope = sampleWithRequiredData;
        expectedResult = service.addScopeToCollectionIfMissing([], scope);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(scope);
      });

      it('should not add a Scope to an array that contains it', () => {
        const scope: IScope = sampleWithRequiredData;
        const scopeCollection: IScope[] = [
          {
            ...scope,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addScopeToCollectionIfMissing(scopeCollection, scope);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Scope to an array that doesn't contain it", () => {
        const scope: IScope = sampleWithRequiredData;
        const scopeCollection: IScope[] = [sampleWithPartialData];
        expectedResult = service.addScopeToCollectionIfMissing(scopeCollection, scope);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(scope);
      });

      it('should add only unique Scope to an array', () => {
        const scopeArray: IScope[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const scopeCollection: IScope[] = [sampleWithRequiredData];
        expectedResult = service.addScopeToCollectionIfMissing(scopeCollection, ...scopeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const scope: IScope = sampleWithRequiredData;
        const scope2: IScope = sampleWithPartialData;
        expectedResult = service.addScopeToCollectionIfMissing([], scope, scope2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(scope);
        expect(expectedResult).toContain(scope2);
      });

      it('should accept null and undefined values', () => {
        const scope: IScope = sampleWithRequiredData;
        expectedResult = service.addScopeToCollectionIfMissing([], null, scope, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(scope);
      });

      it('should return initial array if no Scope is added', () => {
        const scopeCollection: IScope[] = [sampleWithRequiredData];
        expectedResult = service.addScopeToCollectionIfMissing(scopeCollection, undefined, null);
        expect(expectedResult).toEqual(scopeCollection);
      });
    });

    describe('compareScope', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareScope(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareScope(entity1, entity2);
        const compareResult2 = service.compareScope(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareScope(entity1, entity2);
        const compareResult2 = service.compareScope(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareScope(entity1, entity2);
        const compareResult2 = service.compareScope(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
