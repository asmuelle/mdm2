import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { MeterDetailComponent } from './meter-detail.component';

describe('Meter Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MeterDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: MeterDetailComponent,
              resolve: { meter: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(MeterDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load meter on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MeterDetailComponent);

      // THEN
      expect(instance.meter).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
